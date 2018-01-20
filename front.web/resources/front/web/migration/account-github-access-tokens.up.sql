create table account_github_access_tokens (
  account_id integer primary key,
  github_account_id integer not null unique,
  access_token varchar(256) not null,
  created_at timestamp not null,
  foreign key (account_id) references accounts(id)
);
