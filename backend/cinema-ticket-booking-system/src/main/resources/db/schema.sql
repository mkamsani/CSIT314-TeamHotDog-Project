/* TODO
 *  user_account
 *  user_profile
 *  tickets
 *  cinema_room
 *  seats
 *  movie_screening
 *  food
 *  drinks
 *  reports
 *  ratings
 *  reviews
 *  loyalty_points
 */

DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA public;
-- Import extension for uuid_generate_v4()
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE user_account
(
    --  Not visible to user:
    numeric_id      SERIAL PRIMARY KEY,
    id              Uuid         NOT NULL UNIQUE DEFAULT uuid_generate_v4(),
    username        VARCHAR(255) NOT NULL UNIQUE, -- .toLowerCase()
    account_type    VARCHAR(255) NOT NULL        DEFAULT 'customer',
    password_hash   VARCHAR(72)  NOT NULL        DEFAULT crypt('password' || 'optional_pepper', gen_salt('bf')),

    --  Visible to user:
    first_name      VARCHAR(255) NOT NULL,
    last_name       VARCHAR(255) NOT NULL,
    email           VARCHAR(255) NOT NULL UNIQUE, -- .toLowerCase()
    address         VARCHAR(255) NOT NULL,
    time_created    TIMESTAMP    NOT NULL        DEFAULT NOW(),
    time_last_login TIMESTAMP    NOT NULL        DEFAULT NOW(),

    --  Constraints:
    CONSTRAINT enum_account_type CHECK (account_type IN ('customer', 'manager', 'owner', 'admin'))
);

CREATE TABLE customer
(
    id             Uuid    NOT NULL PRIMARY KEY,
    customer_id    SERIAL  NOT NULL,
    -- TRUE if customer is active, FALSE if customer is deactivated.
    account_status BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (id) REFERENCES user_account (id) ON DELETE CASCADE
);

CREATE TABLE movie
(
    id             SERIAL PRIMARY KEY,
    title          VARCHAR(255)   NOT NULL,
    description    VARCHAR(255)   NOT NULL,
    release_date   DATE           NOT NULL,
    content_rating VARCHAR(255)   NOT NULL, -- .toLowerCase()
    price          NUMERIC(10, 2) NOT NULL,
    imdb_url       VARCHAR(255) UNIQUE,
    CONSTRAINT enum_rating CHECK (content_rating IN ('g', 'pg', 'pg13', 'nc16', 'm18', 'r21'))
);

CREATE TABLE movie_session
(
    id        SERIAL PRIMARY KEY,
    movie_id  INTEGER      NOT NULL,
    show_time VARCHAR(255) NOT NULL,
    CONSTRAINT enum_show_time CHECK (show_time IN ('morning', 'afternoon', 'evening', 'midnight')),
    CONSTRAINT fk_movie_id FOREIGN KEY (movie_id) REFERENCES movie (id)
);

CREATE TABLE cinema_room
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(255) NOT NULL,
    capacity INTEGER      NOT NULL
);

CREATE TABLE seats
(
    cinema_room INTEGER NOT NULL,
    row         CHAR(1) NOT NULL PRIMARY KEY,
    number      INTEGER NOT NULL,
    booked      INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT seat_unique UNIQUE (cinema_room, row, number),
    CONSTRAINT fk_cinema_room FOREIGN KEY (cinema_room) REFERENCES cinema_room (id)
);

CREATE TABLE loyalty_points
(
    id      SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    points  INTEGER NOT NULL,
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES user_account (numeric_id)
);

CREATE TABLE food
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL,
    description VARCHAR(255)   NOT NULL,
    price       NUMERIC(10, 2) NOT NULL
);

CREATE TABLE drinks
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL,
    description VARCHAR(255)   NOT NULL,
    price       NUMERIC(10, 2) NOT NULL
);

CREATE TABLE tickets
(
    id            SERIAL PRIMARY KEY,
    movie_session INTEGER NOT NULL,
    cinema_room   INTEGER NOT NULL,
    seat_row      CHAR    NOT NULL,
    seat_number   INTEGER NOT NULL,
    user_id       INTEGER NOT NULL,
    food          INTEGER,
    drink         INTEGER,
    CONSTRAINT fk_movie_session FOREIGN KEY (movie_session) REFERENCES movie_session (id),
    CONSTRAINT fk_cinema_room FOREIGN KEY (cinema_room) REFERENCES cinema_room (id),
    CONSTRAINT fk_seat_row FOREIGN KEY (seat_row) REFERENCES seats (row),
    CONSTRAINT fk_seat_number FOREIGN KEY (seat_number) REFERENCES seats (number),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES user_account (numeric_id),
    CONSTRAINT fk_food FOREIGN KEY (food) REFERENCES food (id),
    CONSTRAINT fk_drink FOREIGN KEY (drink) REFERENCES drinks (id)
);

CREATE TABLE ticket_student
(
    ticket_id INTEGER NOT NULL,
    CONSTRAINT fk_ticket_id FOREIGN KEY (ticket_id) REFERENCES tickets (id)
);

CREATE TABLE ticket_adult
(
    ticket_id INTEGER NOT NULL,
    CONSTRAINT fk_ticket_id FOREIGN KEY (ticket_id) REFERENCES tickets (id)
);

CREATE TABLE ticket_senior
(
    ticket_id INTEGER NOT NULL,
    CONSTRAINT fk_ticket_id FOREIGN KEY (ticket_id) REFERENCES tickets (id)
);

CREATE TABLE ticket_child
(
    ticket_id INTEGER NOT NULL,
    CONSTRAINT fk_ticket_id FOREIGN KEY (ticket_id) REFERENCES tickets (id)
);


CREATE TABLE ratings
(
    id       SERIAL PRIMARY KEY,
    movie_id INTEGER NOT NULL,
    user_id  INTEGER NOT NULL,
    rating   INTEGER NOT NULL,
    CONSTRAINT fk_movie_id FOREIGN KEY (movie_id) REFERENCES movie (id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES user_account (numeric_id)
);


CREATE TABLE reviews
(
    id       SERIAL PRIMARY KEY,
    movie_id INTEGER      NOT NULL,
    user_id  INTEGER      NOT NULL,
    review   VARCHAR(255) NOT NULL,
    CONSTRAINT fk_movie_id FOREIGN KEY (movie_id) REFERENCES movie (id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES user_account (numeric_id)
);

CREATE VIEW monthly_report AS
SELECT movie.title,
       movie.description,
       movie.release_date,
       movie.content_rating,
       movie.price,
       movie.imdb_url
FROM movie
WHERE movie.release_date > NOW() - INTERVAL '1 month';