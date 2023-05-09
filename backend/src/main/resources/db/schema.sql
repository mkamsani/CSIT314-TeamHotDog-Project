/**
This file is the SSOT (Single Source of Truth) for the application.
It contains the tables for the database, to be converted to Java entities.

Datatypes will conform to SQL standard, with the following exceptions:
-- UUID        is used instead of CHAR(36).
-- Timestamptz is used instead of TIMESTAMP WITH TIME ZONE.
Reference:
https://www.postgresql.org/docs/current/datatype.html#DATATYPE-TABLE
http://www.contrib.andrew.cmu.edu/~shadow/sql/sql1992.txt (Ctrl + F "4.1  Data types")
https://www.depesz.com/2010/03/02/charx-vs-varcharx-vs-varchar-vs-text/

For standardisation of date and time, TIMESTAMP WITH TIME ZONE is used for
for all date and time related fields. Where time is not required, the time
will be truncated in Java.
Reference:
https://www.baeldung.com/jpa-java-time
https://www.cockroachlabs.com/blog/time-data-types-postgresql/#conclusion

All table names are in lowercase:     'ticket',      not 'Ticket'.
All table names are in snake_case:    'ticket_type', not 'ticketType'.
All table names are in singular form: 'ticket_type', not 'ticket_types'.
In Java, the table name will be converted to camelCase: 'ticketType'.
Reference:
https://www.oracle.com/java/technologies/javase/codeconventions-namingconventions.html
https://www.postgresql.org/docs/current/sql-syntax-lexical.html#SQL-SYNTAX-IDENTIFIERS

All CHECK/UNIQUE constraints are also validated in Java.
This is to fulfil the requirement of the project,
which is to handle all logic in the backend.

Certain CREATE TRIGGER and CREATE FUNCTION statements (or part of the statement) are temporary,
and will be removed once the Java code is implemented.
These statements will be marked with a 'TO-DO: Convert to Spring Boot'.
TO-DO will not contain a hyphen in the actual comments below.

| TABLE/VIEW             | CRUD  | Status      | Notes
|------------------------|-------|-------------|--------------------------------
| user_profile           | CRUS  | In progress |
| user_account           | CRUS  | In progress | Delete == is_active FALSE.
| loyalty_point          |  R    | Not started | 100% generated. Java update.
| rating_review          | CRUD  | Not started | Depends on loyalty_point.
| movie                  | CRUD  | In progress |
| cinema_room            |  RUS  | In progress | Create when db is initialised.
| screening              | CRUD  | In progress | Depends on movie, cinema_room
| ticket_type            | CRUS  | In progress | Create when db is initialised.
| ticket                 | CRUD  | In progress | Depends on screening, seat
| seat                   |  R    | In progress | 100% generated.
| monthly_rating_report  |  R    | Not started | TODO.
| monthly_revenue_report |  R    | Not started | TODO.
| food_order             | CRUD  | Not started | Not implemented.
| food_combo             | CRUD  | Not started | Not implemented.
*/

DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA public;
-- Import extension for uuid_generate_v4()
-- Use gen_random_uuid() if not importing uuid-ossp.
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS pgcrypto;
CREATE EXTENSION IF NOT EXISTS citext;

/*
 * In schema.sh,
 *   "-e POSTGRES_INITDB_ARGS" is used to set the locale.
 *   "-e TZ"                   is used to set the timezone.
 * Here, we confirm that the timezone and locale are set correctly.
 * A SELECT on the CURRENT_SETTING() function is used to get the value.
 * Alternatively, we can use: SHOW TIMEZONE; SHOW lc_time; SHOW lc_monetary;
 */
SELECT CURRENT_SETTING('TIMEZONE')    AS timezone,
       CURRENT_SETTING('lc_time')     AS lc_time,
       CURRENT_SETTING('lc_monetary') AS lc_monetary;

CREATE TABLE user_profile
(
  PRIMARY KEY (uuid),
  uuid      Uuid         NOT NULL DEFAULT uuid_generate_v4(),
  is_active BOOLEAN      NOT NULL DEFAULT TRUE, -- Suspend a profile.
  privilege VARCHAR(255) NOT NULL CHECK ( privilege IN ('customer', 'manager', 'owner', 'admin')),
  title     VARCHAR(255) NOT NULL UNIQUE,
  CHECK (title != 'customer' OR privilege = 'customer')
);
CREATE INDEX ON user_profile (privilege);
CREATE INDEX ON user_profile (title);

CREATE TABLE user_account
(
  PRIMARY KEY (uuid),
  uuid            Uuid        NOT NULL DEFAULT uuid_generate_v4(),
  is_active       BOOLEAN     NOT NULL DEFAULT TRUE, -- Suspend an account.
  user_profile    Uuid        NOT NULL REFERENCES user_profile (uuid) ON UPDATE CASCADE,
  password_hash   VARCHAR(72) NOT NULL CHECK (LENGTH(password_hash) <= 72 ),

  /* The columns BELOW are visible to the user. */
  username        Citext       NOT NULL UNIQUE,        -- Alphanumeric and used for login.
  email           Citext       NOT NULL UNIQUE,
  first_name      VARCHAR(255) NOT NULL,
  last_name       VARCHAR(255) NOT NULL,
  address         VARCHAR(255) NOT NULL,               -- Users can enter their address in any format.
  date_of_birth   DATE         NOT NULL,               -- SELECT EXTRACT(YEAR FROM AGE(NOW(), date_of_birth)) FROM user_account;
  time_created    Timestamptz  NOT NULL DEFAULT NOW(), -- Long-term user benefits.
  time_last_login Timestamptz  NOT NULL DEFAULT NOW()  -- User inactivity.
);

CREATE TABLE loyalty_point
(
  PRIMARY KEY (uuid),
  uuid            Uuid    NOT NULL REFERENCES user_account (uuid) ON DELETE CASCADE ON UPDATE CASCADE,
  points_redeemed INTEGER NOT NULL DEFAULT 0 CHECK (points_redeemed >= 0),
  points_total    INTEGER NOT NULL DEFAULT 0 CHECK (points_total    >= 0)
);

-- Pre-generate 20 movies, allow manager to INSERT into this table
CREATE TABLE movie
(
  PRIMARY KEY (uuid),
  uuid           Uuid         NOT NULL DEFAULT uuid_generate_v4(),
  is_active      BOOL         NOT NULL, -- Suspends a movie.
  title          VARCHAR(255) NOT NULL,
  genre          VARCHAR(255) NOT NULL, -- .toLowerCase() wikipedia
  description    VARCHAR(255) NOT NULL,
  release_date   DATE         NOT NULL,
  image_url      VARCHAR(255) NOT NULL DEFAULT 'https://raw.githubusercontent.com/assets/default.jpg',
  landscape_image_url VARCHAR(255) NOT NULL DEFAULT 'https://raw.githubusercontent.com/assets/default.jpg',
  content_rating VARCHAR(255) NOT NULL,
  CHECK (content_rating IN ('g', 'pg', 'pg13', 'nc16', 'm18', 'r21'))
);

CREATE TABLE cinema_room
(
  PRIMARY KEY (id),
  id        INTEGER NOT NULL CHECK (id > 0 AND id <= 8), -- Room number, max 8.
  is_active BOOLEAN NOT NULL    DEFAULT TRUE             -- FALSE if room is under maintenance.
);

CREATE TABLE screening
(
  PRIMARY KEY (uuid),
  uuid        Uuid       NOT NULL DEFAULT    uuid_generate_v4(),
  is_active   BOOLEAN    NOT NULL DEFAULT    TRUE,
  movie_id    Uuid       NOT NULL REFERENCES movie (uuid),
  cinema_room INTEGER    NOT NULL REFERENCES cinema_room (id),

  -- Java:     if (screening.getShowDate().isBefore(LocalDate.now())) { screening.setIsActive(false); }
  -- Postgres: CREATE FUNCTION + CREATE TRIGGER TODO
  show_date   DATE       NOT NULL,
  show_time   VARCHAR(9) NOT NULL    CHECK      (show_time IN ('morning', 'afternoon', 'evening', 'midnight'))
);

-- TRIGGER generates 280 seats, after every INSERT INTO cinema_room.
-- 14 row * 20 column = 280 seats.
-- Stops at N because 'O' is easily mistaken for '0'.
CREATE TABLE seat
(
  PRIMARY KEY (uuid),
  uuid          Uuid    NOT NULL DEFAULT uuid_generate_v4(),
  cinema_room   INTEGER NOT NULL REFERENCES cinema_room (id),
  seat_row      CHAR(1) NOT NULL CHECK (seat_row >= 'A' AND seat_row <= 'N'),
  seat_column   INTEGER NOT NULL CHECK (seat_column >= 1 AND seat_column <= 20),
  UNIQUE (cinema_room, seat_row, seat_column)
);

-- The price can be changed.
-- Manager can create new ticket types.
CREATE TABLE ticket_type
(
  PRIMARY KEY (uuid),
  uuid       Uuid           NOT NULL DEFAULT uuid_generate_v4(),
  type_name  TEXT           NOT NULL UNIQUE,                  -- Defaults: ('adult', 'student', 'child', 'senior')
  type_price NUMERIC(10, 2) NOT NULL CHECK (type_price >= 0), -- e.g. 10.50
  is_active  BOOLEAN        NOT NULL DEFAULT TRUE
);

CREATE TABLE ticket
(
  PRIMARY KEY (uuid),
  uuid          Uuid        NOT NULL DEFAULT    uuid_generate_v4(),
  customer      Uuid        NOT NULL REFERENCES user_account (uuid),
  ticket_type   TEXT        NOT NULL REFERENCES ticket_type (type_name),
  screening     Uuid        NOT NULL REFERENCES screening (uuid),
  seat          Uuid        NOT NULL REFERENCES seat (uuid),
  purchase_date Timestamptz NOT NULL DEFAULT    NOW(),

  -- Composite superkey
  -- this.seat == that.seat && this.screening == that.screening { "booked." }
  UNIQUE (seat, screening)
);

-----------------------------------------------------------------------------------------------------------------------
-- Do not edit anything below until we are done with what's above.
-----------------------------------------------------------------------------------------------------------------------

-- We are not going to be doing food_combo and food_order.
CREATE TABLE food_combo
(
  PRIMARY KEY (uuid),
  uuid        Uuid           DEFAULT uuid_generate_v4(),
  description TEXT           NOT NULL    UNIQUE,            -- e.g. "Popcorn and Coke"
  price       NUMERIC(10, 2) NOT NULL    CHECK (price >= 0) -- e.g. 10.00
);

-- Not doing this.
-- Frontend should handle ticket purchase,
-- send data to us,
-- then redirect customer to optional food_order purchase.
CREATE TABLE food_order
(
    PRIMARY KEY (uuid),
  uuid         Uuid NOT NULL DEFAULT uuid_generate_v4(),
  combo_number Uuid NOT NULL REFERENCES food_combo (uuid),

  -- For consideration, no ideas for functionalities with order_time yet:
  ticket       Uuid NOT NULL REFERENCES ticket (uuid)
  -- order_time   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
);


-- Rating review will be done with along with loyalty.
CREATE TABLE rating_review
(
  PRIMARY KEY (uuid),
  uuid   Uuid    NOT NULL REFERENCES loyalty_point (uuid),
  rating INTEGER NOT NULL CHECK (rating > 0 AND rating <= 5),
  review TEXT    NOT NULL CHECK (LENGTH(review) > 0) -- e.g. "The cinema's popcorn is the best!"
);

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
