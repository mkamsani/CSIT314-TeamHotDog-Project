CREATE USER u_unprivileged WITH PASSWORD 'uu';
-- The default user postgres is a superuser.
CREATE ROLE r_owner WITH PASSWORD 'ro';
CREATE ROLE r_manager WITH PASSWORD 'rm';
CREATE ROLE r_customer WITH PASSWORD 'rc';
-- Import extension for uuid_generate_v4()
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE movie
(
    id           SMALLSERIAL PRIMARY KEY,
    title        VARCHAR(255)   NOT NULL,
    description  VARCHAR(255)   NOT NULL,
    release_date DATE           NOT NULL,
    rating       VARCHAR(255)   NOT NULL,
    price        NUMERIC(10, 2) NOT NULL,
    imdb_url     VARCHAR(255) UNIQUE,
    CONSTRAINT enum_rating CHECK (rating IN ('g', 'pg', 'pg13', 'nc16', 'm18', 'r21'))
);

CREATE TABLE movie_session
(
    id        SERIAL PRIMARY KEY,
    movie_id  INT       NOT NULL,
    show_time TIMESTAMP NOT NULL,
    CONSTRAINT fk_movie_id FOREIGN KEY (movie_id) REFERENCES movie (id)
);

CREATE TABLE seat
(
    id     SERIAL PRIMARY KEY,
    row    INT NOT NULL,
    number INT NOT NULL,
    CONSTRAINT seat_unique UNIQUE (row, number)
);

CREATE TABLE loyalty_points
(
    id      SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    points  INT NOT NULL,
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES user_account (numeric_id)
);