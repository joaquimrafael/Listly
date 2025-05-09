const axiosInstance = axios;
const { useState, useEffect } = React;


//import { useEffect, useState } from "react";

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

    useEffect(() => {
        //axios.get("https://localhost:8443/api/lists")
        axios.get("https://44.214.113.103:8443/api/lists")
            .then(response => {
                setLists(response.data); // Agora as listas são exibidas na ordem recebida
            })
            .catch(error => console.error("Erro ao buscar listas", error));
    }, []);

    return (
        <div className="container">
            <h1>Suas Listas</h1>
            {lists.map((list) => (
                <div key={list.id} className="list-item">
                    <h3>{list.name}</h3>
                    <button onClick={() => {
                        setSelectedList(list.id);
                        setScreen("list");
                    }}>
                        Ver Itens
                    </button>
                </div>
            ))}
        </div>
    );
};
    

const ListScreen = () => { 
    const items = [ { name: "Playstation 5", price: "USD 300", description: "Console de jogos", priority: "Médio", link: "#" }, 
                    { name: "Barbie", price: "USD 110", description: "Boneca de brinquedo", priority: "Alto", link: "#" }, 
                    { name: "Laptop", price: "USD 700", description: "Computador", priority: "Baixo", link: "#" } 
    ];
    
    return ( 
        <div className="container"> 
            <h1>Lista de Presentes</h1> 
            {items.map((item, index) => ( 
                <div className="list-item" key={index}> 
                    <h3>{item.name}</h3> 
                    <p>Preço: {item.price}</p> 
                    <p>Descrição: {item.description}</p> 
                    <p>Prioridade: {item.priority}</p> 
                    <a href={item.link}>Ver mais</a> 
                    <button>Editar</button> 
                    <button>Excluir</button> 
                </div> 
            ))} 
            <button>Criar novo item</button> 
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
            {screen === "lists" && <ListsScreen setScreen={setScreen}  />}
            {screen === "list"  && <ListScreen />}
            {screen === "profile" && <Profile />} 
        </div>
    ); 
};
    



// Renderizando o aplicativo no navegador
ReactDOM.render(<App />, document.getElementById("root"));
