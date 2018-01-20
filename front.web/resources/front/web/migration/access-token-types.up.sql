create table access_token_types (
  id integer primary key,
  name varchar(256) not null,
  description text
);

insert into access_token_types (id, name) values (1, 'github');
