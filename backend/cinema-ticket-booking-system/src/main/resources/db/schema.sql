/* TODO
 *  User Stories:
 *  [x] user_account
 *  [ ] user_profile
 *  [ ] tickets
 *  [ ] cinema_room
 *  [ ] movie_screening
 *  [ ] food_order
 *  [ ] reports
 *  [ ] ratings
 *  [ ] reviews
 *  [ ] loyalty_points
 *  Non-user stories:
 *  [ ] seats (not a user story)
 */

DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA public;
-- Import extension for uuid_generate_v4()
-- Use gen_random_uuid() if not importing uuid-ossp.
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS pgcrypto;
/* Docs:
https://www.postgresql.org/docs/current/datatype.html#DATATYPE-TABLE
*/

CREATE TABLE user_profile
(
    uuid      Uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_type VARCHAR(255) NOT NULL, -- Only these 4: ('customer', 'manager', 'owner', 'admin')

    -- these role types should not be for generalized names like 'staff' or 'employee'
    role_type VARCHAR(255),          -- Examples: (e.g. 'senior manager', 'senior admin', 'intern manager')
    CHECK (user_type IN ('customer', 'manager', 'owner', 'admin')),
    -- check that if it is customer, then role_type is null
    CHECK (user_type != 'customer' AND role_type IS NOT NULL
        OR user_type = 'customer' AND role_type IS NULL)
);

CREATE TABLE user_account
(
    --  Not visible to user:
    uuid            Uuid PRIMARY KEY                  DEFAULT uuid_generate_v4(),
    password_hash   VARCHAR(72)              NOT NULL,
    user_profile    Uuid                     NOT NULL,

    --  Visible to user:
    username        VARCHAR(255)             NOT NULL UNIQUE,
    first_name      VARCHAR(255)             NOT NULL,
    last_name       VARCHAR(255)             NOT NULL,
    email           VARCHAR(255)             NOT NULL UNIQUE,
    address         VARCHAR(255)             NOT NULL,
    time_created    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    time_last_login TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    date_of_birth   DATE                     NOT NULL,
    -- date from now minus date of birth to obtain age, for ticket types.

    -- Java String.toLowerCase()
    CHECK (username = LOWER(username)),
    CHECK (email = LOWER(email)),
    FOREIGN KEY (user_profile) REFERENCES user_profile (uuid) ON DELETE CASCADE
);

CREATE TABLE loyalty_points
(
    uuid            Uuid PRIMARY KEY,
    -- TRUE if customer is active, FALSE if customer is deactivated.
    user_profile    Uuid                     NOT NULL,
    user_type       VARCHAR(255) NOT NULL,
    points_redeemed INTEGER      NOT NULL DEFAULT 0,
    points_total    INTEGER      NOT NULL DEFAULT 0,
    CHECK (points_redeemed >= 0),
    CHECK (points_total >= 0),
    FOREIGN KEY (uuid) REFERENCES user_account (uuid) ON DELETE CASCADE,
    FOREIGN KEY (user_profile) REFERENCES user_profile (uuid) ON DELETE CASCADE,
    CHECK (user_type IN ('customer'))
);

CREATE TABLE movie
(
    id             SERIAL PRIMARY KEY,
    title          VARCHAR(255) NOT NULL,
    genre          VARCHAR(255) NOT NULL, -- .toLowerCase()
    description    VARCHAR(255) NOT NULL, -- wikipedia
    release_date   DATE         NOT NULL, -- wikipedia
    content_rating VARCHAR(255) NOT NULL, -- .toLowerCase()
    created_by     INTEGER      NOT NULL,
    CHECK (content_rating IN ('g', 'pg', 'pg13', 'nc16', 'm18', 'r21'))
    -- for future consideration, pull actual ratings from rotten tomatoes or other sites.
    -- with an external api.
);

CREATE TABLE cinema_room
(
    id       SERIAL PRIMARY KEY, -- room number, max 8.
    capacity INTEGER NOT NULL,
    CHECK (capacity >= 0),
    CHECK (id > 0 AND id <= 8)
    --  For future consideration:
    --  status BOOLEAN NOT NULL DEFAULT TRUE, -- FALSE if room is under maintenance.
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
    seat_row      CHAR(1)      NOT NULL, -- Max. A to J
    seat_column   INTEGER      NOT NULL, -- Max. 20
    status        VARCHAR      NOT NULL DEFAULT 'available',
    seat_type     VARCHAR(255) NOT NULL,
    seat_category VARCHAR(255) NOT NULL,
    UNIQUE (cinema_room, seat_row, seat_column),
    FOREIGN KEY (cinema_room) REFERENCES cinema_room (id),
    CHECK (status IN ('available', 'booked', 'pending', 'unavailable')),
    CHECK (seat_type IN ('normal', 'wheelchair', 'disabled')),
    CHECK (seat_category IN ('normal', 'gold')),
    CHECK (seat_row >= 'A' AND seat_row <= 'J'),
    CHECK (seat_column >= 1 AND seat_column <= 20)
);

CREATE TABLE ticket_types
(
    type_name    VARCHAR(255) PRIMARY KEY,
    type_price   NUMERIC(10, 2)           NOT NULL,
    last_updated TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_by   INTEGER                  NOT NULL,
    CHECK (type_name IN ('Adult', 'Student', 'Child', 'Senior')),
    CHECK (type_price >= 0)
    -- For consideration, should we allow the changing of prices,
    -- or allow a discount system.
    -- A discount system would mean that all four types will have fixed prices.
    -- then, the discount will modify the price value.
);

CREATE TABLE tickets
(
    id            SERIAL PRIMARY KEY,
    customer      Uuid                     NOT NULL,
    ticket_type   VARCHAR(255)             NOT NULL,
    movie_session INTEGER                  NOT NULL,
    seat          INTEGER                  NOT NULL,
    purchase_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    FOREIGN KEY (customer) REFERENCES loyalty_points (uuid),
    FOREIGN KEY (movie_session) REFERENCES movie_session (id),
    FOREIGN KEY (seat) REFERENCES seats (id),
    FOREIGN KEY (ticket_type) REFERENCES ticket_types (type_name),
    -- The seat and movie_session must be unique.
    UNIQUE (seat, movie_session)
);

-- There are only five different food combos.
CREATE TABLE food_combo
(
    id          INTEGER PRIMARY KEY,
    description VARCHAR(255)   NOT NULL, -- e.g. "Popcorn and Coke"
    price       NUMERIC(10, 2) NOT NULL, -- e.g. 10.00
    CHECK (id > 0 AND id <= 5)
);

CREATE TABLE food_order
(
    id           SERIAL PRIMARY KEY,
    user_account uuid NOT NULL,
    combo_number INTEGER NOT NULL,
    ticket       INTEGER NOT NULL,
    FOREIGN KEY (user_account) REFERENCES user_account (uuid),
    FOREIGN KEY (combo_number) REFERENCES food_combo (id),
    FOREIGN KEY (ticket) REFERENCES tickets (id)
    -- For consideration, no ideas for functionalities with order_time yet:
    -- order_time   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
);

CREATE TABLE rating_review
(
    uuid     uuid PRIMARY KEY,
    rating INTEGER NOT NULL,
    review TEXT    NOT NULL,
    CHECK (rating IN (1, 2, 3, 4, 5)),
    FOREIGN KEY (uuid) REFERENCES loyalty_points (uuid)
);

-- Only 'R' in 'CRUD' is implemented.
CREATE VIEW monthly_revenue_report AS
SELECT tickets.purchase_date::DATE  AS date,
       tickets.ticket_type          AS type,
       ticket_types.type_price      AS price,
       SUM(ticket_types.type_price) AS total_revenue, -- pick one
       COUNT(tickets.ticket_type)   AS total_tickets  -- of these two
FROM tickets
         JOIN ticket_types ON tickets.ticket_type = ticket_types.type_name
WHERE tickets.purchase_date::DATE > NOW() - INTERVAL '1 month'
GROUP BY tickets.purchase_date::DATE, tickets.ticket_type, ticket_types.type_price
ORDER BY tickets.purchase_date::DATE DESC;
/*
reasons:
 1
 for not implementing as a table/ java entity:
 owner should not change the values in the report.
 ^ justify why we use a view when asked in class.
 2
*/

-- SELECT *
-- FROM monthly_revenue_report WHERE tickets.purchase_date::DATE > NOW() - INTERVAL '1 week' ;
-- FROM monthly_revenue_report WHERE tickets.purchase_date::DATE > NOW() - INTERVAL '1 day' ;

/*
for every ticket bought save the current date
another table: for every month, check how many tickets are bought, have a counter: in this table or another table
output this value to put into the report
person generating can put from month to month, this number will be in the report

purpose:
- which month has the most customers,
- knowing which month
- certain months has lower customer
- boss can launch promotional events/sales
- e.g. customer buys more in X month

non-purpose:
- which movie is most popular
*/