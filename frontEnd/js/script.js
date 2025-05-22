const axiosInstance = axios;
const { useState, useEffect } = React; 
const generateUniqueId = () => {
    return Math.floor(Math.random() * 1000000); // 🔹 Gera um número inteiro aleatório
};



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
    const [items, setItems] = useState([]);

    useEffect(() => {
        axios.get("https://localhost:8443/api/lists")
            .then(response => {
                setLists(response.data);
            })
            .catch(error => console.error("Erro ao buscar listas", error));
    }, []);

    const handleDeleteItem = (listId, productId) => {
        axios.delete(`https://localhost:8443/api/items?listId=${listId}&productId=${productId}`)
            .then(() => {
                setItems(prevItems => prevItems.filter(item => item.listId !== listId || item.productId !== productId));
            })
            .catch(error => console.error("Erro ao excluir item", error));
    };

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
                        axios.get(`https://localhost:8443/api/items?listId=${list.id}`)
                            .then(response => {
                                const items = response.data || [];
                                console.log("Itens antes da exclusão:", items);

                                if (!items || items.length === 0) {
                                    console.log("Nenhum item na lista, excluindo diretamente...");
                                    return axios.delete(`https://localhost:8443/api/lists/${list.id}`)
                                        .then(() => setLists(lists.filter(l => l.id !== list.id)))
                                        .catch(error => console.error("Erro ao excluir lista", error));
                                }

                                const deleteItemRequests = items.map(item => {
                                    console.log(`Excluindo item: listId=${list.id}, productId=${item.productId}`);
                                    return axios.delete(`https://localhost:8443/api/items?listId=${list.id}&productId=${item.productId}`);
                                });

                                return Promise.all(deleteItemRequests)
                                    .then(() => {
                                        console.log("Todos os itens excluídos. Agora, recarregando a lista...");
                                        return new Promise(resolve => setTimeout(resolve, 500)); // 🔹 Aguarda 500ms antes de excluir a lista
                                    })
                                    .then(() => axios.delete(`https://localhost:8443/api/lists/${list.id}`))
                                    .then(() => {
                                        console.log("Lista excluída com sucesso!");
                                        setLists(prevLists => prevLists.filter(l => l.id !== list.id));
                                    })
                                    .catch(error => console.error("Erro ao excluir itens ou lista", error));
                            })
                            .catch(error => console.error("Erro ao buscar itens da lista", error));
                    }}>
                        Excluir Lista
                    </button>
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
                    <button className="back-button" onClick={() => setShowCreateInput(false)}>Cancelar</button>
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
    const [products, setProducts] = useState([]);
    const [quantity, setQuantity] = useState(1);
    const [newProduct, setNewProduct] = useState({ name: "", price: 0, description: "", priority: "", link: "" });
    const [view, setView] = useState("items");
    const [selectedProduct, setSelectedProductLocal] = useState(null);
    const [showAddOptions, setShowAddOptions] = useState(false);


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

        axios.get("https://localhost:8443/api/products")
            .then(response => {
                setProducts(response.data);
            })
            .catch(error => console.error("Erro ao buscar produtos disponíveis", error));

    }, [selectedList]);

    const handleDeleteItem = (listId, productId) => {
        axios.delete(`https://localhost:8443/api/items?listId=${listId}&productId=${productId}`)
            .then(() => {
                setItems(prevItems => prevItems.filter(item => item.listId !== listId || item.productId !== productId));
            })
            .catch(error => console.error("Erro ao excluir item", error));
    };

    const handleAddExistingProduct = () => {
        if (!selectedProduct) {
            alert("Selecione um produto!");
            return;
        }
        const parsedQuantity = parseInt(quantity, 10); // 🔹 Garante que seja um número inteiro
        if (isNaN(parsedQuantity) || parsedQuantity <= 0) {
            alert("Erro: A quantidade deve ser um número inteiro positivo!");
            return;
        }

        console.log("Enviando dados:", {
            listId: selectedList.id,
            productId: selectedProduct.id,
            quantity: parsedQuantity
        });


        axios.post(`https://localhost:8443/api/items?listId=${selectedList.id}&productId=${selectedProduct.id}&quantity=${parsedQuantity}`)
            .then(() => {
                setItems([...items, { 
                    listId: selectedList.id, 
                    productId: selectedProduct.id, 
                    product: selectedProduct, 
                    quantity: parsedQuantity 
                }]); // 🔹 Garante que o item tenha `listId` e `productId`
                setShowAddOptions(false);
                setView("items");
            })
            .catch(error => console.error("Erro ao adicionar item", error));

    };

    const handleCreateNewProduct = () => {
        const newProductId = generateUniqueId(); // 🔹 Agora geramos um ID único manualmente

        const productData = {
            id: newProductId, // 🔹 Passando o ID no request body
            name: newProduct.name,
            price: parseFloat(newProduct.price), 
            description: newProduct.description,
            priority: newProduct.priority,
            link: newProduct.link
        };

        axios.post("https://localhost:8443/api/products", productData)
            .then(response => {
                const createdProduct = response.data;

                return axios.post(`https://localhost:8443/api/items?listId=${selectedList.id}&productId=${createdProduct.id}&quantity=${quantity}`)
                    .then(() => createdProduct);
            })
            .then(createdProduct => {
                setItems([...items, { 
                    listId: selectedList.id, 
                    productId: createdProduct.id, 
                    product: createdProduct, 
                    quantity 
                }]); // 🔹 Agora `listId` e `productId` estão sendo armazenados corretamente

                setView("items"); // Volta para a tela inicial
            })
            .catch(error => console.error("Erro ao criar produto e item", error));

    };


    return (
        <div className="container">
            <h1>Itens da {selectedList && selectedList.name ? selectedList.name : "Lista Desconhecida"}</h1>

            {/* 📌 Tela principal: Exibe apenas os itens, botão de adicionar novo item e botão de voltar */}
            {view === "items" && (
                <div>
                    {items.length > 0 ? (
                        items.map((item) => (
                            <div className="list-item" key={item.id}>
                                <h3>{item.product && item.product.name ? item.product.name : "Nome desconhecido"}</h3>
                                <p>Quantidade: {item.quantity}</p>
                                <button onClick={() => {
                                    setSelectedProduct(item.product);
                                    setScreen("product");
                                }}>Ver mais</button>
                                <button className="delete-button" onClick={() => handleDeleteItem(item.listId, item.productId)}>🗑️</button>
                            </div>
                        ))
                    ) : (
                        <p>Nenhum item encontrado.</p>
                    )}
                    <button onClick={() => setView("addOptions")}>Adicionar Novo Item</button>
                    <button className="back-button" onClick={() => setScreen("lists")}>Voltar</button>
                </div>
            )}

            {/* 📌 Tela de opções ao clicar em "Adicionar Novo Item" */}
            {view === "addOptions" && (
                <div>
                    <button onClick={() => setView("addExisting")}>Adicionar Produto Existente</button>
                    <button onClick={() => setView("createProduct")}>Criar Novo Produto</button>
                    <button className="back-button" onClick={() => setView("items")}>Voltar</button>
                </div>
            )}

            {/* 📌 Tela de adicionar produto existente */}
            {view === "addExisting" && (
                <div>
                    <h3>Selecione um produto:</h3>
                    <select onChange={(e) => {
                        const selected = products.find(p => p.id === parseInt(e.target.value));
                        if (!selected) {
                            console.error("Erro: Produto não encontrado!");
                            return;
                        }
                        setSelectedProductLocal(selected);
                    }}>
                        <option value="">Selecione...</option>
                        {products && items ? (
                            products
                                .filter(p => !items.some(item => item.product && item.product.id === p.id)) // Evita erro de undefined em item.product
                                .map(product => (
                                    <option key={product.id} value={product.id}>
                                        {product.name}
                                    </option>
                                ))
                        ) : (
                            <option disabled>Carregando produtos...</option>
                    )}
                    </select>
                    <input 
                        type="number" 
                        value={quantity} 
                        onChange={(e) => setQuantity(Math.max(1, parseInt(e.target.value, 10) || 1))} 
                        min="1" 
                    />
                    <button onClick={handleAddExistingProduct}>Adicionar</button>
                    <button className="back-button" onClick={() => setView("addOptions")}>Cancelar</button>
                </div>
            )}

            {/* 📌 Tela de criação de novo produto */}
            {view === "createProduct" && (
                <div>
                    <input type="text" placeholder="Nome" value={newProduct.name} onChange={(e) => setNewProduct({...newProduct, name: e.target.value})} />
                    <input type="number" placeholder="Preço" value={newProduct.price} onChange={(e) => setNewProduct({...newProduct, price: e.target.value})} />
                    <input type="text" placeholder="Descrição" value={newProduct.description} onChange={(e) => setNewProduct({...newProduct, description: e.target.value})} />
                    <select 
                        value={newProduct.priority} 
                        onChange={(e) => setNewProduct({...newProduct, priority: e.target.value})}
                    >
                        <option value="" disabled>Selecione a prioridade</option>
                        <option value="High">High</option>
                        <option value="Medium">Medium</option>
                        <option value="Low">Low</option>
                    </select>
                    <input type="text" placeholder="Link (Opcional)" value={newProduct.link} onChange={(e) => setNewProduct({...newProduct, link: e.target.value})} />
                    <button onClick={handleCreateNewProduct}>Salvar</button>
                    <button className="back-button" onClick={() => setView("addOptions")}>Cancelar</button>
                </div>
            )}
        </div>
    );


};

const ProductScreen = ({ selectedProduct, setScreen, selectedList }) => {
    const [view, setView] = useState("default"); // Estado que controla a visualização
    const [newQuantity, setNewQuantity] = useState(selectedProduct && selectedProduct.quantity || 1);
    const [editedProduct, setEditedProduct] = useState({ ...selectedProduct });

    if (!selectedProduct) return <p>Produto não encontrado.</p>;

    const handleUpdateQuantity = () => {
        const parsedQuantity = parseInt(newQuantity, 10);
        if (isNaN(parsedQuantity) || parsedQuantity <= 0) {
            alert("Erro: A quantidade deve ser um número inteiro positivo!");
            return;
        }

        console.log("selectedProduct:", selectedProduct);
        console.log("selectedProduct.id:", selectedProduct && selectedProduct.id);
        console.log("selectedList.id:", selectedList && selectedList.id);
        console.log("newQuantity:", newQuantity);


        axios.put(`https://localhost:8443/api/items?listId=${selectedList.id}&productId=${selectedProduct.id}&quantity=${parsedQuantity}`)
            .then(() => {
                alert("Quantidade atualizada com sucesso!");
                setView("default"); // Volta para a tela inicial após atualização
            })
            .catch(error => console.error("Erro ao atualizar quantidade", error));
    };


    const handleUpdateProduct = () => {
        axios.put(`https://localhost:8443/api/products/${selectedProduct.id}`, editedProduct)
            .then(() => {
                alert("Produto atualizado com sucesso!");
                setView("default"); // Volta para a tela inicial após atualização
            })
            .catch(error => console.error("Erro ao atualizar produto", error));
    };

    if (!selectedProduct) {
        return (
            <div className="container">
                <h1>Produto não encontrado.</h1>
                <button className="back-button" onClick={() => setScreen("list")}>Voltar</button>
            </div>
        );
    }

    return (
        <div className="product-container">
            <h1 className="product-title">{selectedProduct.name}</h1>

            {/* 📌 Tela principal: Exibe as informações do produto */}
            {view === "default" && (
                <div>
                    <p className="product-price"><strong>Preço:</strong> {selectedProduct.price}</p>
                    <p className="product-description"><strong>Descrição:</strong> {selectedProduct.description || "Sem descrição disponível."}</p>
                    <p className="product-priority"><strong>Prioridade:</strong> {selectedProduct.priority || "Não definida."}</p>
                    <div className="product-links">
                        <strong>Links:</strong>
                        {selectedProduct.link ? (
                            Array.isArray(selectedProduct.link) ? (
                                selectedProduct.link.map((link, index) => (
                                    <a key={index} href={link} target="_blank" rel="noopener noreferrer" className="product-link">
                                        Link {index + 1}
                                    </a>
                                ))
                            ) : (
                                <a href={selectedProduct.link} target="_blank" rel="noopener noreferrer" className="product-link">
                                    Link do produto
                                </a>
                            )
                        ) : (
                            <p>Sem links para esse produto</p>
                        )}
                    </div>
                    <button onClick={() => setView("editOptions")}>Editar</button>
                    <button className="back-button" onClick={() => setScreen("list")}>Voltar</button>
                </div>
            )}

            {/* 📌 Tela de opções de edição */}
            {view === "editOptions" && (
                <div>
                    <button onClick={() => setView("editQuantity")}>Editar Quantidade</button>
                    <button onClick={() => setView("editProduct")}>Editar Produto</button>
                    <button className="back-button" onClick={() => setView("default")}>Voltar</button>
                </div>
            )}

            {/* 📌 Tela para editar quantidade */}
            {view === "editQuantity" && (
                <div>
                    <h3>Nova Quantidade:</h3>
                    <input 
                        type="number" 
                        value={newQuantity} 
                        onChange={(e) => setNewQuantity(Math.max(1, parseInt(e.target.value, 10) || 1))} 
                        min="1" 
                    />
                    <button onClick={handleUpdateQuantity}>Salvar</button>
                    <button className="back-button" onClick={() => setView("editOptions")}>Cancelar</button>
                </div>
            )}

            {/* 📌 Tela para editar informações do produto */}
            {view === "editProduct" && (
                <div>
                    <input type="text" placeholder="Nome" value={editedProduct.name} onChange={(e) => setEditedProduct({...editedProduct, name: e.target.value})} />
                    <input type="number" placeholder="Preço" value={editedProduct.price} onChange={(e) => setEditedProduct({...editedProduct, price: e.target.value})} />
                    <input type="text" placeholder="Descrição" value={editedProduct.description} onChange={(e) => setEditedProduct({...editedProduct, description: e.target.value})} />
                    <select 
                        value={editedProduct.priority} 
                        onChange={(e) => setEditedProduct({...editedProduct, priority: e.target.value})}
                    >
                        <option value="" disabled>Selecione a prioridade</option>
                        <option value="High">High</option>
                        <option value="Medium">Medium</option>
                        <option value="Low">Low</option>
                    </select>
                    <input type="text" placeholder="Link (Opcional)" value={editedProduct.link} onChange={(e) => setEditedProduct({...editedProduct, link: e.target.value})} />
                    <button onClick={handleUpdateProduct}>Salvar</button>
                    <button className="back-button" onClick={() => setView("editOptions")}>Cancelar</button>
                </div>
            )}
        </div>
    );

};

const Profile = () => {
    const [isEditing, setIsEditing] = useState(false);
    const [name, setName] = useState("Usuário Exemplo");
    const [email, setEmail] = useState("usuario@email.com");

    return (
        <div className="container">
            {isEditing ? (
                // 🔹 Modo edição: Exibe apenas o formulário, botão de salvar e cancelar
                <div>
                    <h2>Editar Perfil</h2>
                    <input 
                        type="text" 
                        value={name} 
                        onChange={(e) => setName(e.target.value)} 
                        placeholder="Nome"
                    />
                    <input 
                        type="email" 
                        value={email} 
                        onChange={(e) => setEmail(e.target.value)} 
                        placeholder="Email"
                    />
                    <button onClick={() => setIsEditing(false)}>Salvar</button>
                    <button className="back-button" onClick={() => setIsEditing(false)}>Cancelar</button>
                </div>
            ) : (
                // 🔹 Modo visualização: Exibe apenas os dados e botão "Editar Perfil"
                <div>
                    <h2>Perfil do Usuário</h2>
                    <p><strong>Nome:</strong> {name}</p>
                    <p><strong>Email:</strong> {email}</p>
                    <button onClick={() => setIsEditing(true)}>Editar Perfil</button>
                </div>
            )}
        </div>
    );
};


// Componente principal com navegação entre telas
const App = () => { 
    const [screen, setScreen] = React.useState("home");
    const [selectedList, setSelectedList] = useState(null);
    const [selectedProduct, setSelectedProduct] = useState(null);


    return ( 
        <div className="app-container"> 
            <nav> 
                <img src="js/Listly.png" alt="Logo" class="navbar-img"></img>
                <div class="nav-buttons-container">
                    <button onClick={() => setScreen("home")}>Home</button> 
                    <button onClick={() => setScreen("lists")}>Lists</button> 
                    <button onClick={() => setScreen("profile")}>Profile</button> 
                </div>
            </nav> 
            {screen === "home" && <Home setScreen={setScreen} />} 
            {screen === "register" && <Register />} 
            {screen === "login" && <Login />} 
            {screen === "lists" && <ListsScreen setScreen={setScreen} setSelectedList={setSelectedList} />}
            {screen === "list"  && selectedList && <ListScreen selectedList={selectedList} setScreen={setScreen} setSelectedProduct={setSelectedProduct} />}
            {screen === "product" && selectedProduct && (
                <ProductScreen selectedProduct={selectedProduct} selectedList={selectedList} setScreen={setScreen} />
            )}
            {screen === "profile" && <Profile />} 
        </div>
    ); 
};

// Renderizando o aplicativo no navegador
ReactDOM.render(<App />, document.getElementById("root"));
