const axiosInstance = axios;
const { useState, useEffect } = React;


// Definição dos componentes

const Register = () => {
  const [form, setForm] = React.useState({ username: '', email: '', password: '' });

  const handleChange = (e) => {
      setForm({ ...form, [e.target.name]: e.target.value });
  };

  return (
      <div className="container">
          <h2>Registro</h2>
          <form>
              <input type="text" name="username" placeholder="Nome de usuário" onChange={handleChange} />
              <input type="email" name="email" placeholder="E-mail" onChange={handleChange} />
              <input type="password" name="password" placeholder="Senha" onChange={handleChange} />
              <button>Registrar</button>
          </form>
      </div>
  );
};


const Login = () => (
  <div className="container">
      <h2>Login</h2>
      <form>
          <input type="email" placeholder="E-mail" />
          <input type="password" placeholder="Senha" />
          <button>Entrar</button>
      </form>
  </div>
);

const Home = ({ setScreen }) => (
  <div className="container home-container">
      <h1>Bem-vindo ao Listly</h1>
      <p>Organize seus presentes e desejos de maneira simples e eficaz.</p>
      <div className="button-group">
          <button className="nav-button" onClick={() => setScreen("lists")}>Ver Listas</button>
          <button className="nav-button" onClick={() => setScreen("profile")}>Ver Perfil</button>
      </div>
  </div>
);

const ListsScreen = ({ setScreen, setSelectedList }) => {
    const [lists, setLists] = useState([]);
    const [editingList, setEditingList] = useState(null);
    const [listName, setListName] = useState("");
    const [showCreateInput, setShowCreateInput] = useState(false);

    useEffect(() => {
        axios.get("https://localhost:8443/api/lists")
            .then(response => {
                setLists(response.data);
            })
            .catch(error => console.error("Erro ao buscar listas", error));
    }, []);

    // Criar nova lista
    const handleCreateList = () => {
        if (!listName.trim()) {
            alert("O nome da lista não pode estar vazio!");
            return;
        }

        axios.post("https://localhost:8443/api/lists", { name: listName })
            .then(response => {
                setLists(prevLists => [...prevLists, response.data]); // Adiciona ao estado
                setListName(""); // Limpa campo
                setShowCreateInput(false); // Esconde input de criação
            })
            .catch(error => console.error("Erro ao criar lista", error));
    };

    // Iniciar edição de uma lista
    const startEditing = (list) => {
        setEditingList(list);
        setListName(list.name); // Preenche o campo com nome atual
    };

    // Salvar edição (PUT)
    const handleEditList = () => {
        if (!listName.trim()) {
            alert("O nome da lista não pode estar vazio!");
            return;
        }

        axios.put(`https://localhost:8443/api/lists/${editingList.id}`, { name: listName })
            .then(() => {
                setLists(prevLists =>
                    prevLists.map(l => (l.id === editingList.id ? { ...l, name: listName } : l))
                );
                setEditingList(null); // Fecha edição
                setListName(""); // Limpa campo
            })
            .catch(error => console.error("Erro ao editar lista", error));
    };

    return (
        <div className="container">
            <h1>Suas Listas</h1>

            {/* Exibir listas apenas quando não estiver criando ou editando */}
            {!showCreateInput && !editingList && lists.map((list) => (
                <div key={list.id} className="list-item">
                    <h3>{list.name}</h3>
                    <button onClick={() => {
                        setSelectedList({id: list.id, name: list.name});
                        setScreen("list");
                    }}>
                        Ver Itens
                    </button>
                    <button onClick={() => startEditing(list)}>Editar Lista</button>
                    <button onClick={() => {
                        axios.delete(`https://localhost:8443/api/lists/${list.id}`)
                            .then(() => {
                                setLists(lists.filter((l) => l.id !== list.id));
                            })
                            .catch(error => console.error("Erro ao excluir lista", error));
                    }}>Excluir Lista</button>
                </div>
            ))}

            {/* Botão para criar nova lista - só aparece se não estiver editando */}
            {!showCreateInput && !editingList && (
                <button onClick={() => setShowCreateInput(true)}>Criar Nova Lista</button>
            )}

            {/* Formulário de criação de nova lista */}
            {showCreateInput && (
                <div>
                    <input 
                        type="text" 
                        placeholder="Nome da lista" 
                        value={listName} 
                        onChange={(e) => setListName(e.target.value)} 
                    />
                    <button onClick={handleCreateList}>Salvar</button>
                    <button onClick={() => setShowCreateInput(false)}>Cancelar</button>
                </div>
            )}

            {/* Formulário de edição de lista */}
            {editingList && (
                <div>
                    <input 
                        type="text" 
                        placeholder="Novo nome da lista" 
                        value={listName} 
                        onChange={(e) => setListName(e.target.value)} 
                    />
                    <button onClick={handleEditList}>Salvar</button>
                    <button onClick={() => setEditingList(null)}>Cancelar</button>
                </div>
            )}
        </div>
    );
};

const ListScreen = ({ setScreen, selectedList, setSelectedProduct }) => {
    const [items, setItems] = useState([]);

    useEffect(() => {
        if (selectedList && selectedList.id) {
            axios.get(`https://localhost:8443/api/items?listId=${selectedList.id}`)
                .then(response => {
                    const itemList = response.data || [];

                    // Agora buscamos os detalhes dos produtos usando o productId de cada item
                    const productRequests = itemList.map(item =>
                        axios.get(`https://localhost:8443/api/products/${item.productId}`)
                            .then(res => ({ ...item, product: res.data }))
                    );

                    Promise.all(productRequests)
                        .then(updatedItems => setItems(updatedItems))
                        .catch(error => console.error("Erro ao buscar produtos", error));
                })
                .catch(error => console.error("Erro ao buscar itens da lista", error));
        }
    }, [selectedList]);

    const handleDeleteItem = (listId, productId) => {
        axios.delete(`https://localhost:8443/api/items?listId=${listId}&productId=${productId}`)
            .then(() => {
                setItems(prevItems => prevItems.filter(item => item.listId !== listId || item.productId !== productId));
            })
            .catch(error => console.error("Erro ao excluir item", error));
    };


    return (
        <div className="container">
            <h1>Itens da {selectedList && selectedList.name ? selectedList.name : "Lista Desconhecida"}</h1>

            {items.length > 0 ? (
                items.map((item) => (
                    <div className="list-item" >
                        <div>
                            <h3>{item.product && item.product.name ? item.product.name : "Nome desconhecido"}</h3>
                            <p>Preço: {item.product && item.product.price ? item.product.price : "N/A"}</p>
                        </div>
                        <button 
                            onClick={() => {setSelectedProduct(item.product); 
                                            setScreen("product");}}
                            style={{ marginRight: "10px" }}
                        >
                            Ver mais
                        </button>

                        <button 
                            onClick={() => handleDeleteItem(item.listId, item.productId)}
                            style={{
                                background: "none",
                                border: "none",
                                cursor: "pointer",
                            }}
                        >  
                            🗑️
                        </button>  
                    </div>    
                ))
            ) : (
                <p>Nenhum item encontrado.</p>
            )}

            <button className="back-button" onClick={() => setScreen("lists")}>Voltar</button>
        </div>
    );
};

const ProductScreen = ({ selectedProduct, setScreen }) => {
    if (!selectedProduct) return <p>Produto não encontrado.</p>;

    return (
        <div className="product-container">
            <h1 className="product-title">{selectedProduct.name}</h1>
            <p className="product-price"><strong>Preço:</strong> {selectedProduct.price}</p>
            <p className="product-description"><strong>Descrição:</strong> {selectedProduct.description || "Sem descrição disponível."}</p>
            <p className="product-priority"><strong>Prioridade:</strong> {selectedProduct.priority || "Não definida"}</p>

            {/* Exibir os links */}
            <div className="product-links">
                <h3>Links do Produto:</h3>
                {selectedProduct.links && selectedProduct.links.length > 0 ? (
                    selectedProduct.links.map((link, index) => (
                        <a key={index} href={link} target="_blank" rel="noopener noreferrer" className="product-link">
                            🔗 Link {index + 1}
                        </a>
                    ))
                ) : (
                    <p>Sem links disponíveis.</p>
                )}
            </div>

            <button className="back-button" onClick={() => setScreen("list")}>Voltar</button>
        </div>
    );
};

const Profile = () => (
  <div className="container">
      <h2>Perfil do Usuário</h2>
      <p>Nome: Usuário Exemplo</p>
      <p>Email: usuario@email.com</p>
      <button>Editar Perfil</button>
  </div>
);

// Componente principal com navegação entre telas
const App = () => { 
    const [screen, setScreen] = React.useState("home");
    const [selectedList, setSelectedList] = useState(null);
    const [selectedProduct, setSelectedProduct] = useState(null);


    return ( 
        <div className="app-container"> 
            <nav> 
                <button onClick={() => setScreen("home")}>🏠</button> 
                <button onClick={() => setScreen("lists")}>📃</button> 
                <button onClick={() => setScreen("profile")}>👤</button> 
            </nav> 
            {screen === "home" && <Home setScreen={setScreen} />} 
            {screen === "register" && <Register />} 
            {screen === "login" && <Login />} 
            {screen === "lists" && <ListsScreen setScreen={setScreen} setSelectedList={setSelectedList} />}
            {screen === "list"  && selectedList && <ListScreen selectedList={selectedList} setScreen={setScreen} setSelectedProduct={setSelectedProduct} />}
            {screen === "product" && selectedProduct && <ProductScreen selectedProduct={selectedProduct} setScreen={setScreen} />}
            {screen === "profile" && <Profile />} 
        </div>
    ); 
};
    



// Renderizando o aplicativo no navegador
ReactDOM.render(<App />, document.getElementById("root"));
