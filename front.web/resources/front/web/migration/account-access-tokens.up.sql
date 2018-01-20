create table account_access_tokens (
  account_id integer not null,
  access_token_type_id integer not null,
  created_at timestamp not null,
  primary key (account_id, access_token_type_id),
  foreign key (account_id) references accounts(id),
  foreign key (access_token_type_id) references access_token_types(id)
);
