/* TODO
 *  [x] user_account
 *  [ ] user_profile
 *  [ ] tickets
 *  [ ] cinema_room
 *  [ ] seats
 *  [ ] movie_screening
 *  [ ] food_order
 *  [ ] reports
 *  [ ] ratings
 *  [ ] reviews
 *  [ ] loyalty_points
 */

DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA public;
-- Import extension for uuid_generate_v4()
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE user_account
(
    --  Not visible to user:
    id              SERIAL PRIMARY KEY,
    username        VARCHAR(255) NOT NULL UNIQUE,
    account_type    VARCHAR(255) NOT NULL DEFAULT 'customer',
    password_hash   VARCHAR(72)  NOT NULL DEFAULT crypt('password' || 'optional_pepper', gen_salt('bf')),

    --  Visible to user:
    first_name      VARCHAR(255) NOT NULL,
    last_name       VARCHAR(255) NOT NULL,
    email           VARCHAR(255) NOT NULL UNIQUE,
    address         VARCHAR(255) NOT NULL,
    time_created    TIMESTAMP    NOT NULL DEFAULT NOW(),
    time_last_login TIMESTAMP    NOT NULL DEFAULT NOW(),
    CHECK (account_type IN ('customer', 'manager', 'owner', 'admin')),

    -- Java String.toLowerCase()
    CHECK (username = LOWER(username)),
    CHECK (email = LOWER(email))
);

CREATE TABLE customer
(
    id             INTEGER PRIMARY KEY,
    -- TRUE if customer is active, FALSE if customer is deactivated.
    account_status BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (id) REFERENCES user_account (id) ON DELETE CASCADE
);

CREATE TABLE movie
(
    id             SERIAL PRIMARY KEY,
    title          VARCHAR(255)   NOT NULL,
    description    VARCHAR(255)   NOT NULL, -- wikipedia
    release_date   DATE           NOT NULL, -- wikipedia
    content_rating VARCHAR(255)   NOT NULL, -- .toLowerCase()
    price          NUMERIC(10, 2) NOT NULL,
    imdb_url       VARCHAR(255) UNIQUE,     -- wikipedia
    CHECK (content_rating IN ('g', 'pg', 'pg13', 'nc16', 'm18', 'r21'))
);

CREATE TABLE cinema_room
(
    id       SERIAL PRIMARY KEY, -- room number, max 8.
    capacity INTEGER NOT NULL,
    CHECK (capacity >= 0),
    CHECK (id > 0 AND id <= 8)
);


CREATE TABLE movie_session
(
    id          SERIAL PRIMARY KEY,
    movie_id    INTEGER    NOT NULL,
    show_time   VARCHAR(9) NOT NULL,
    cinema_room INTEGER    NOT NULL,
    CHECK (show_time IN ('morning', 'afternoon', 'evening', 'midnight')),
    FOREIGN KEY (movie_id) REFERENCES movie (id),
    FOREIGN KEY (cinema_room) REFERENCES cinema_room (id)
);


CREATE TABLE seats
(
    id            SERIAL PRIMARY KEY,
    cinema_room   INTEGER      NOT NULL,
    seat_row      CHAR(1)      NOT NULL,
    seat_column   INTEGER      NOT NULL,
    status        VARCHAR      NOT NULL DEFAULT 'available',
    seat_type     VARCHAR(255) NOT NULL,
    seat_category VARCHAR(255) NOT NULL,
    UNIQUE (cinema_room, seat_row, seat_column),
    FOREIGN KEY (cinema_room) REFERENCES cinema_room (id),
    CHECK (status IN ('available', 'booked', 'pending', 'unavailable')),
    CHECK (seat_type IN ('normal', 'wheelchair', 'disabled')),
    CHECK (seat_category IN ('normal', 'gold'))
);

CREATE TABLE loyalty_points
(
    id              SERIAL PRIMARY KEY,
    user_id         INTEGER NOT NULL,
    points_redeemed INTEGER NOT NULL DEFAULT 0,
    points_total    INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES user_account (id),
    CHECK (points_redeemed >= 0),
    CHECK (points_total >= 0)
);

-- There are only five different food combos.
CREATE TABLE food_combo
(
    id          INTEGER PRIMARY KEY,
    description VARCHAR(255)   NOT NULL, -- e.g. "Popcorn and Coke"
    price       NUMERIC(10, 2) NOT NULL, -- e.g. 10.00
    CHECK ( id > 0 AND id <= 5)
);

CREATE TABLE food_order
(
    id           SERIAL PRIMARY KEY,
    user_id      INTEGER   NOT NULL,
    combo_number INTEGER   NOT NULL,
    order_time   TIMESTAMP NOT NULL DEFAULT NOW(),
    FOREIGN KEY (user_id) REFERENCES user_account (id),
    FOREIGN KEY (combo_number) REFERENCES food_combo (id)
);

CREATE TABLE tickets
(
    id            SERIAL PRIMARY KEY,
    movie_session INTEGER   NOT NULL,
    cinema_room   INTEGER   NOT NULL,
    seat          INTEGER   NOT NULL,
    purchase_date TIMESTAMP NOT NULL DEFAULT NOW(),
    FOREIGN KEY (movie_session) REFERENCES movie_session (id),
    FOREIGN KEY (cinema_room) REFERENCES cinema_room (id),
    FOREIGN KEY (seat) REFERENCES seats (id)
);

CREATE TABLE ticket_adult
(
    ticket_id INTEGER        NOT NULL,
    price     NUMERIC(10, 2) NOT NULL,
    FOREIGN KEY (ticket_id) REFERENCES tickets (id)
);

CREATE TABLE ticket_student
(
    ticket_id INTEGER        NOT NULL,
    price     NUMERIC(10, 2) NOT NULL,
    FOREIGN KEY (ticket_id) REFERENCES tickets (id)
);


CREATE TABLE ticket_senior
(
    ticket_id INTEGER        NOT NULL,
    price     NUMERIC(10, 2) NOT NULL,
    FOREIGN KEY (ticket_id) REFERENCES tickets (id)
);

CREATE TABLE ticket_child
(
    ticket_id INTEGER        NOT NULL,
    price     NUMERIC(10, 2) NOT NULL,
    FOREIGN KEY (ticket_id) REFERENCES tickets (id)
);

CREATE TABLE ratings
(
    id           SERIAL PRIMARY KEY,
    movie        INTEGER NOT NULL,
    user_account INTEGER NOT NULL,
    rating       INTEGER NOT NULL,
    FOREIGN KEY (movie) REFERENCES movie (id),
    FOREIGN KEY (user_account) REFERENCES user_account (id),
    CHECK (rating IN (1, 2, 3, 4, 5))
);

CREATE TABLE reviews
(
    id      SERIAL PRIMARY KEY,
    movie   INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    review  TEXT    NOT NULL,
    FOREIGN KEY (movie) REFERENCES movie (id)

    -- TODO: Which user_id to use?
    --  FOREIGN KEY (user_id) REFERENCES user_account (numeric_id)
    --  FOREIGN KEY (user_id) REFERENCES customer (id)
);

CREATE VIEW monthly_revenue_report AS
SELECT tickets.purchase_date::DATE AS date,
       SUM(ticket_adult.price)     AS adult_revenue,
       SUM(ticket_student.price)   AS student_revenue,
       SUM(ticket_senior.price)    AS senior_revenue,
       SUM(ticket_child.price)     AS child_revenue,
       SUM(ticket_adult.price +
           ticket_student.price +
           ticket_senior.price +
           ticket_child.price)     AS total_revenue
FROM tickets
         JOIN ticket_adult ON tickets.id = ticket_adult.ticket_id
         JOIN ticket_student ON tickets.id = ticket_student.ticket_id
         JOIN ticket_senior ON tickets.id = ticket_senior.ticket_id
         JOIN ticket_child ON tickets.id = ticket_child.ticket_id
WHERE tickets.purchase_date::DATE > NOW() - INTERVAL '1 month'
GROUP BY tickets.purchase_date::DATE
ORDER BY tickets.purchase_date::DATE;

CREATE VIEW yearly_revenue_report AS
SELECT EXTRACT(YEAR FROM tickets.purchase_date) AS year,
       SUM(ticket_adult.price)                  AS adult_revenue,
       SUM(ticket_student.price)                AS student_revenue,
       SUM(ticket_senior.price)                 AS senior_revenue,
       SUM(ticket_child.price)                  AS child_revenue,
       SUM(ticket_adult.price +
           ticket_student.price +
           ticket_senior.price +
           ticket_child.price)                  AS total_revenue
FROM tickets
         JOIN ticket_adult ON tickets.id = ticket_adult.ticket_id
         JOIN ticket_student ON tickets.id = ticket_student.ticket_id
         JOIN ticket_senior ON tickets.id = ticket_senior.ticket_id
         JOIN ticket_child ON tickets.id = ticket_child.ticket_id
WHERE tickets.purchase_date::DATE > NOW() - INTERVAL '1 year'
GROUP BY EXTRACT(YEAR FROM tickets.purchase_date)
ORDER BY EXTRACT(YEAR FROM tickets.purchase_date);