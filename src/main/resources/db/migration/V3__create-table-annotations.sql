create table annotations(

    id bigint not null auto_increment,
    description varchar(255) not null,
    completed boolean not null,
    user_id bigint not null,

    primary key(id),
    constraint fk_annotations_user_id foreign key(user_id) references users(id)

);