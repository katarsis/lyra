insert into roles (id, role) values (1,'ROLE_USER');
insert into roles (id, role) values (2,'ROLE_ADMIN');
insert into user_account(id,passwd, user_email, user_login) values(1,'qwe','piton@piton', 'piton');
insert into user_roles(id_user,id_role) values (1,1);
insert into user_roles(id_user, id_role) values (1,2);