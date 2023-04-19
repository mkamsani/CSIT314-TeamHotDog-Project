/**
 * This file is the SSOT (Single Source of Truth) for the application.
 * It contains the tables for the database, to be converted to Java entities.
 *
 * Reference:
 * https://www.postgresql.org/docs/current/datatype.html#DATATYPE-TABLE
 * http://www.contrib.andrew.cmu.edu/~shadow/sql/sql1992.txt (4.1  Data types)
 *
 * For standardisation of date and time, TIMESTAMP WITH TIME ZONE is used for
 * for all date and time related fields. Where time is not required, the time
 * will be truncated in Java.
 *
 * All table names are in lowercase:     'ticket',      not 'Ticket'.
 * All table names are in snake_case:    'ticket_type', not 'ticketType'.
 * All table names are in singular form: 'ticket_type', not 'ticket_types'.
 * In Java, the table name will be converted to camelCase: 'ticketType'.
 *
 * All CHECK/UNIQUE constraints are also validated in Java.
 * This is to fulfil the requirement of the project,
 * which is to handle all logic in the backend.
 *
 * user_profile     |  CRUD  |  In progress  |
 * user_account     |  CRUD  |  Not started  |  Delete = suspend account.
 * loyalty_point    |   RU   |  Not started  |  No user-interaction on update.
 * movie            |  CRUD  |  Not started  |
 * cinema_room      |   RU   |  Not started  |  Create when db is initialised.
 * movie_session    |  CRUD  |  Not started  |
 * ticket_type      |  CRUD  |  Not started  |  Create when db is initialised.
 * ticket           |  CRUD  |  Not started  |
 * food_combo       |  CRUD  |  Not started  |
 * food_order       |  CRUD  |  Not started  |
 * rating_review    |  CRUD  |  Not started  |
 * report           |   R    |  Not started  |
 * seat             |   R    |  Not started  |  Create when db is initialised.
 */

DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA public;
-- Import extension for uuid_generate_v4()
-- Use gen_random_uuid() if not importing uuid-ossp.
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS pgcrypto;
-- Confirm settings made (see schema.sh)
SHOW TIMEZONE;
SHOW lc_time;
SHOW lc_monetary;

CREATE TABLE user_profile
(
    /*
     * uuid_generate_v4() is indicated as a fallback.
     * In Java, UUID.randomUUID() is used instead.
     */
    uuid      Uuid PRIMARY KEY DEFAULT uuid_generate_v4(),

    privilege VARCHAR(255) NOT NULL,
    CHECK (privilege IN ('customer', 'manager', 'owner', 'admin')),

    /*
     * These titles should not be for generalized names like 'staff' or 'employee'.
     * Use professional titles, like 'senior manager', 'senior admin', 'intern manager'.
     * However, a user admin can ignore these rules.
     */
    title     VARCHAR(255) NOT NULL UNIQUE,

    /*
     * A customer's title should only be 'Customer'.
     * Deliberate capitalisation on title is to differentiate from the privilege type.
     * The information is also passed to frontend in capitalised form.
     */
    CHECK (privilege != 'customer' OR title = 'Customer')
);
-- Insert default user profiles.
INSERT INTO user_profile (privilege, title)
VALUES ('customer', 'Customer'),
       ('manager', 'Junior Manager'),
       ('manager', 'Senior Manager'),
       ('owner', 'Director of Operations'),
       ('owner', 'Chief Executive Officer'),
       ('admin', 'Junior Admin'),
       ('admin', 'Senior Admin');

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

CREATE TABLE loyalty_point
(
    uuid            Uuid PRIMARY KEY,
    points_redeemed INTEGER NOT NULL DEFAULT 0,
    points_total    INTEGER NOT NULL DEFAULT 0,
    CHECK (points_redeemed >= 0),
    CHECK (points_total >= 0),
    FOREIGN KEY (uuid) REFERENCES user_account (uuid) ON DELETE CASCADE
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

CREATE TABLE screening
(
    id          SERIAL PRIMARY KEY,
    movie_id    INTEGER    NOT NULL,
    show_time   VARCHAR(9) NOT NULL,
    cinema_room INTEGER    NOT NULL,
    CHECK (show_time IN ('morning', 'afternoon', 'evening', 'midnight')),
    FOREIGN KEY (movie_id) REFERENCES movie (id),
    FOREIGN KEY (cinema_room) REFERENCES cinema_room (id)
);

CREATE TABLE seat
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

CREATE TABLE ticket_type
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

CREATE TABLE ticket
(
    id            SERIAL PRIMARY KEY,
    customer      Uuid                     NOT NULL,
    ticket_type   VARCHAR(255)             NOT NULL,
    movie_session INTEGER                  NOT NULL,
    seat          INTEGER                  NOT NULL,
    purchase_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    FOREIGN KEY (customer) REFERENCES loyalty_point (uuid),
    FOREIGN KEY (movie_session) REFERENCES screening (id),
    FOREIGN KEY (seat) REFERENCES seat (id),
    FOREIGN KEY (ticket_type) REFERENCES ticket_type (type_name),
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
    user_account Uuid    NOT NULL,
    combo_number INTEGER NOT NULL,
    ticket       INTEGER NOT NULL,
    FOREIGN KEY (user_account) REFERENCES user_account (uuid),
    FOREIGN KEY (combo_number) REFERENCES food_combo (id),
    FOREIGN KEY (ticket) REFERENCES ticket (id)
    -- For consideration, no ideas for functionalities with order_time yet:
    -- order_time   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
);

CREATE TABLE rating_review
(
    uuid   Uuid PRIMARY KEY,
    rating INTEGER NOT NULL,
    review TEXT    NOT NULL,
    CHECK (rating IN (1, 2, 3, 4, 5)),
    FOREIGN KEY (uuid) REFERENCES loyalty_point (uuid)
);

-- Only 'R' in 'CRUD' is implemented.
CREATE VIEW monthly_revenue_report AS
SELECT ticket.purchase_date::DATE  AS date,
       ticket.ticket_type          AS type,
       ticket_type.type_price      AS price,
       SUM(ticket_type.type_price) AS total_revenue, -- pick one
       COUNT(ticket.ticket_type)   AS total_tickets  -- of these two
FROM ticket
         JOIN ticket_type ON ticket.ticket_type = ticket_type.type_name
WHERE ticket.purchase_date::DATE > NOW() - INTERVAL '1 month'
GROUP BY ticket.purchase_date::DATE, ticket.ticket_type, ticket_type.type_price
ORDER BY ticket.purchase_date::DATE DESC;
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