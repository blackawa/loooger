create table accounts (
  id serial primary key,
  name varchar(256) not null,
  created_at timestamp not null
);
