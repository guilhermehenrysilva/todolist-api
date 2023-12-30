create table users(

    id bigint not null auto_increment,
    email varchar(100) unique not null,
    password varchar(255),
    name varchar(255) not null,
    auth_client_type varchar(255) not null,

    primary key(id)

);