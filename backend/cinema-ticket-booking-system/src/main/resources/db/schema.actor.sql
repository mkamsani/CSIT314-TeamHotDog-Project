create user u_unprivileged with password 'uu';
-- The default user postgres is a superuser.
create role r_owner with password 'ro';
create role r_manager with password 'rm';
create role r_customer with password 'rc';

create table user_account
(
    id              int primary key,
    username        varchar(255) not null,
    role            varchar(255) not null,
    first_name      varchar(255) not null,
    last_name       varchar(255) not null,
    email           varchar(255) not null unique,
    phone           varchar(255) not null unique,
    address         varchar(255) not null,
    time_created    timestamp    not null,
    time_last_login timestamp    not null
);

create table user_password
(
    id                  int primary key,
    password            varchar(255) not null,
    time_last_attempt   timestamp    not null,
    status_last_attempt boolean      not null,
    CONSTRAINT fk_id FOREIGN KEY (id) REFERENCES user_account (id)
);

create table user_profile
(
    -- TODO: Figure out what to put here
    -- TODO: add user profile fields
);

create table owner
(
    id int primary key,
    CONSTRAINT fk_id FOREIGN KEY (id) REFERENCES user_account (id)
);

CREATE TABLE manager
(
    id         INTEGER PRIMARY KEY,
    user_id    INTEGER NOT NULL UNIQUE,
    department VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES user_account (id) ON DELETE CASCADE
);

CREATE TABLE child
(
    id          INTEGER PRIMARY KEY,
    customer_id INTEGER NOT NULL UNIQUE,
    parent_id   INTEGER NOT NULL,
    age         INTEGER,
    FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE CASCADE,
    FOREIGN KEY (parent_id) REFERENCES customer (id) ON DELETE CASCADE
);

CREATE TABLE adult
(
    id          INTEGER PRIMARY KEY,
    customer_id INTEGER NOT NULL UNIQUE,
    FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE CASCADE
);

CREATE TABLE elderly
(
    id               INTEGER PRIMARY KEY,
    customer_id      INTEGER NOT NULL UNIQUE,
    health_condition VARCHAR(50),
    FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE CASCADE
);

