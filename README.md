## 💻 Sobre o projeto

**TodoList-api** é uma API Rest que permite inserir, editar, remover, detalhar e listar todos os afazeres de um usuário logado. Seja ele registrado pela propria API ou através de sua conta google.

Spring Security - Login Form: 

- http://localhost:8080/login
- http://localhost:8080/logout

---

## ⚙️ Funcionalidades

- [x] Autenticação pela API;
- [x] Autenticação OAuth2;
- [x] CRUD de anotações por usuário;

---

## ⚙️ Configurações
OAuth2 - Google Cloud Console: 

https://console.cloud.google.com/apis/credentials?project=dogwood-seeker-344219 

Passar ID do cliente & Chave secreta do cliente como variavel de ambiente do host.

`${SSO_GOOGLE_CLIENT_ID}` & `${SSO_GOOGLE_CLIENT_SECRET}`

---

## 🛠 Tecnologias

As seguintes tecnologias foram utilizadas no desenvolvimento da API Rest do projeto:

- **[Java 17](https://www.oracle.com/java)** A versão mais recente do Java, trazendo melhorias de performance e recursos modernos.
- **[Spring Boot 3](https://spring.io/projects/spring-boot)** Framework para desenvolvimento rápido de aplicações Java, facilitando a criação de APIs e microsserviços.
- **[Maven](https://maven.apache.org)** Ferramenta de automação de builds e gerenciamento de dependências.
- **[MySQL](https://www.mysql.com)** Sistema de gerenciamento de banco de dados relacional utilizado para persistência de dados.
- **[Hibernate](https://hibernate.org)** Framework ORM (Object-Relational Mapping) para facilitar a interação com o banco de dados.
- **[Flyway](https://flywaydb.org)** Ferramenta para gerenciamento de migrações de banco de dados, permitindo versionamento e controle das alterações.
- **[Lombok](https://projectlombok.org)** Biblioteca para reduzir a verbosidade do código Java, automatizando a criação de getters, setters, construtores e outros métodos comuns.

---

## 👤 Autor
Feito com ❤️ por Guilherme Silva.  
📂 Confira mais projetos no meu GitHub: guilhermehenrysilva.
