--
-- sudo -u postgres psql -p 55555 -d templatesite_db -f conf/database.sql
--
DROP TABLE IF EXISTS message;
DROP TABLE IF EXISTS account;
DROP TYPE IF EXISTS account_role;

CREATE TYPE account_role AS ENUM (
    'normal',
    'admin'
);

CREATE TABLE account (
    id serial PRIMARY KEY,
    name text NOT NULL,
    email text UNIQUE NOT NULL,
    password text NOT NULL,
    role account_role NOT NULL,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now()
);

CREATE TABLE message (
    id serial PRIMARY KEY,
    content text NOT NULL,
    tag_list text[] NOT NULL,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now()
);

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO dziva;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO dziva;

-- bcrypted password values are password in both users
INSERT INTO account (name, email, role, password) values ('Admin User', 'admin@tetrao.eu', 'admin', '$2a$10$8K1p/a0dL1LXMIgoEDFrwOfMQbLgtnOoKsWc.6U6H0llP3puzeeEu');
INSERT INTO account (name, email, role, password) values ('Bob Minion', 'bob@tetrao.eu', 'normal', '$2a$10$8K1p/a0dL1LXMIgoEDFrwOfMQbLgtnOoKsWc.6U6H0llP3puzeeEu');
INSERT INTO message (content, tag_list) values ('Welcome to the templatesite!', '{"welcome", "first message", "english"}');

-- SNC

create table goods (
    id              serial primary key,
    price           numeric not null,
    qnt             int not null,
    category        varchar(20) not null,
    title           varchar(50) not null,
    producedBy      varchar(255),
    tradeMark       varchar(50),
    description     varchar(255) not null,
    cars            varchar(255),
    codeID          varchar(50),
    codes           varchar(255),
    state           varchar(20)
    )
;




insert into goods values (1, 12.4, 4, 'Компрессор',
    'Toyota', 'Компрессор 88320-42110 R', 'MSG Rebuilding',
    'Компрессор восст. Toyota RAV 4 II 2.0D 01-', 'Toyota',
    '8831042210, 8832042110', '15236, 240943, 32634G, 510215, 700510215, 8FK351114401, 92030169, ACP314, DCP50032, K15236, T55830, TOK437', 'new');
insert into goods values (2, 12.4, 3, 'Компрессор',
    'SUBARU', 'Компрессор 2107', 'Snc',
    'Компрессор Subaru Forester 2007',
    'Subaru', '8831042210, 8832042110',
    '15236, 240943, 32634G, 510215, 700510215, 8FK351114401, 92030169, ACP314, DCP50032, K15236, T55830, TOK437',
    'new');
insert into goods values (3, 125.4, 3, 'Фильтр',
    'Saab', 'Фильтр - осушитель', 'SNC',
    'Очень  хороший фильтр.',
    'Saab', '12А-4343, 8832042110',
    '5236, 243, 334G, 10215, 700510215',
    'new');
insert into goods values (4, 123.4, 3, 'Компрессор',
    'ЗАЗ-Deawo', 'Компрессор 75', 'Dziva Inc',
    'Пользуясь случаем хочу передать , всем всем все кого я знаю и не знаю. Мира всем !!!',
    'ЗАЗ', '12А-4343, 8832042110',
    '56, 243, 334G, 10215, 700510215',
    'old');