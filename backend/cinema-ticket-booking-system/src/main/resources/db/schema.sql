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

| TABLE/VIEW             | CRUD | Status      | Notes
|------------------------|------|-------------|--------------------------------
| user_profile           | CRUS | In progress |
| user_account           | CRUS | In progress | Delete == is_active FALSE.
| loyalty_point          |  R   | In progress | No user-interaction on update.
| movie                  | CRUD | Not started |
| ticket_type            | CRU  | Not started | Create when db is initialised.
| food_combo             | CRUD | Not started |
| cinema_room            |  RUS | Not started | Create when db is initialised.
| seat                   |  R   | Not started | Create when db is initialised.
| screening              | CRUD | Not started | Depends on movie, cinema_room
| ticket                 | CRUD | Not started | Depends on screening, seat
| food_order             | CRUD | Not started | Depends on food_combo, ticket
| rating_review          | CRUD | Not started | Depends on loyalty_point
| monthly_revenue_report |  R   | Not started |
| monthly_rating_report  |  R   | Not started |
*/

DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA public;
-- Import extension for uuid_generate_v4()
-- Use gen_random_uuid() if not importing uuid-ossp.
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS pgcrypto;

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
  uuid      Uuid             DEFAULT uuid_generate_v4() PRIMARY KEY,
  is_active BOOLEAN NOT NULL DEFAULT TRUE, -- Suspend a profile.
  privilege TEXT    NOT NULL CHECK ( privilege IN ('customer', 'manager', 'owner', 'admin')),

  -- ❎ 'staff', 'employee', 'personnel'.
  -- ✅ 'senior manager', 'senior admin', 'intern manager'.
  title     TEXT    NOT NULL UNIQUE,
  CHECK (title != 'customer' OR privilege = 'customer')
);
CREATE INDEX ON user_profile (privilege);
CREATE INDEX ON user_profile (title);

CREATE TABLE user_account
(
  uuid            Uuid PRIMARY KEY     DEFAULT uuid_generate_v4(),
  is_active       BOOLEAN     NOT NULL DEFAULT TRUE,  -- Suspend an account.
  user_profile    Uuid        NOT NULL REFERENCES user_profile (uuid) ON UPDATE CASCADE,
  password_hash   VARCHAR(72) NOT NULL CHECK (LENGTH(password_hash) <= 72 ),

  /* The columns BELOW are visible to the user. */

  username        TEXT        NOT NULL UNIQUE,        -- Alphanumeric and used for login.
  email           TEXT        NOT NULL UNIQUE,
  first_name      TEXT        NOT NULL,
  last_name       TEXT        NOT NULL,
  address         TEXT        NOT NULL,               -- Users can enter their address in any format.
  date_of_birth   DATE        NOT NULL,               -- SELECT EXTRACT(YEAR FROM AGE(NOW(), date_of_birth)) FROM user_account;
  time_created    Timestamptz NOT NULL DEFAULT NOW(), -- Long-term user benefits.
  time_last_login Timestamptz NOT NULL DEFAULT NOW()  -- User inactivity.
);

CREATE TABLE loyalty_point
(
  uuid            Uuid PRIMARY KEY REFERENCES user_account (uuid),
  points_redeemed INTEGER NOT NULL DEFAULT 0 CHECK (points_redeemed >= 0),
  points_total    INTEGER NOT NULL DEFAULT 0 CHECK (points_total >= 0)
);

CREATE OR REPLACE FUNCTION password_crypt()
  RETURNS TRIGGER
  LANGUAGE plpgsql
AS
$$
BEGIN
  IF (tg_op = 'INSERT') OR (old.password_hash != new.password_hash) THEN
    new.password_hash = crypt(new.password_hash, gen_salt('bf'));
  END IF;
  RETURN new;
END;
$$;
CREATE OR REPLACE TRIGGER password_crypt
  BEFORE INSERT OR UPDATE
  ON user_account
  FOR EACH ROW
EXECUTE PROCEDURE password_crypt();

/*
 * If a user_account has the privilege 'customer' in user_profile,
 * A customer's loyalty_point will be created.
 */
CREATE OR REPLACE FUNCTION loyalty_point_create()
  RETURNS TRIGGER
  LANGUAGE plpgsql
AS
$$
BEGIN
  IF (SELECT privilege FROM user_profile WHERE uuid = new.user_profile) = 'customer' THEN
    INSERT INTO loyalty_point
      (uuid)
    VALUES
      (new.uuid);
  END IF;
  RETURN new;
END;
$$;
CREATE OR REPLACE TRIGGER loyalty_point_create
  AFTER INSERT
  ON user_account
  FOR EACH ROW
EXECUTE PROCEDURE loyalty_point_create();

/*
How to check for password_hash:

SELECT EXISTS
(
  SELECT password_hash FROM user_account
  WHERE  username      = 'stonebraker'
  AND    is_active     = TRUE -- Suspended accounts cannot login.
  AND    password_hash = crypt('password_Adm_is_%CIO', password_hash)
);
-- 'true'

SELECT EXISTS
(
  SELECT password_hash FROM user_account
  WHERE  username      = 'stonebraker'
  AND    is_active     = TRUE -- Suspended accounts cannot login.
  AND    password_hash = crypt('wrong_password', password_hash)
);
-- 'false'
-- (incorrect username / account suspended / incorrect password)
*/

-- Pre-generate 20 movies, allow manager to INSERT into this table
CREATE TABLE movie
(
  id             SERIAL PRIMARY KEY,
  title          TEXT NOT NULL,
  genre          TEXT NOT NULL,                                                       -- .toLowerCase() wikipedia
  description    TEXT NOT NULL,                                                       -- .toLowerCase() wikipedia
  release_date   DATE NOT NULL,                                                       -- .toLowerCase() wikipedia
  image_url      TEXT DEFAULT 'https://raw.githubusercontent.com/assets/default.jpg', -- .toLowerCase()
  content_rating TEXT NOT NULL,                                                       -- .toLowerCase()
  CHECK (content_rating IN ('g', 'pg', 'pg13', 'nc16', 'm18', 'r21'))
  -- for future consideration, pull actual ratings from rotten tomatoes or other sites.
  -- with an external api.
);

-- Terrence said no need to create.
CREATE TABLE cinema_room
(
  id       SERIAL PRIMARY KEY CHECK (id > 0 AND id <= 8), -- room number, max 8.
  capacity INTEGER NOT NULL CHECK (capacity >= 0)
  --  For future consideration:
  --  status BOOLEAN NOT NULL DEFAULT TRUE, -- FALSE if room is under maintenance.
);

-- A manager should handle the INSERT of movies into this table.
CREATE TABLE screening
(
  id            SERIAL     NOT NULL PRIMARY KEY,
  movie_id      INTEGER    NOT NULL REFERENCES movie (id),
  show_time     VARCHAR(9) NOT NULL CHECK (show_time IN ('morning', 'afternoon', 'evening', 'midnight')),
  cinema_room   INTEGER    NOT NULL REFERENCES cinema_room (id),
  is_active     BOOLEAN,

  /*
   * In Java, we filter dates that are lesser than today/now()
   * In Database, we do it as a trigger, that will set all dates
   * lesser than today to FALSE for "is_active"
   */
  date_of_movie DATE       NOT NULL
);

-- Pre-generated 280 seats, per cinema room.
CREATE TABLE seat
(
  id            SERIAL PRIMARY KEY,
  cinema_room   INTEGER NOT NULL REFERENCES cinema_room (id) ON DELETE CASCADE,

  -- 14 row * 20 column = 280 seats.
  -- Stops at N because 'O' is easily mistaken for '0'.
  seat_row      CHAR(1) NOT NULL CHECK (seat_row >= 'A' AND seat_row <= 'N'),
  seat_column   INTEGER NOT NULL CHECK (seat_column >= 1 AND seat_column <= 20),

  -- Additional enhancements, can remove if not implemented.
  seat_type     TEXT    NOT NULL CHECK (seat_type IN ('normal', 'wheelchair', 'disabled')),
  -- Additional enhancements, can remove if not implemented.
  seat_category TEXT    NOT NULL CHECK (seat_category IN ('normal', 'gold')),

  UNIQUE (cinema_room, seat_row, seat_column)
);

CREATE TABLE ticket_type
(
  uuid       Uuid    DEFAULT uuid_generate_v4(),
  -- pregenerate ('adult', 'student', 'child', 'senior')
  type_name  VARCHAR(7) UNIQUE,
  type_price NUMERIC(10, 2) NOT NULL CHECK (type_price >= 0), -- 10.50
  is_active  BOOLEAN DEFAULT TRUE

  -- why generate uuid or auto-increment primary key?
  -- UPDATE ticket_type set type_name 'student' WHERE type_name = 'teenager'
  -- UPDATE ticket_type set type_name 'adult' WHERE type_name = 'student'
  -- UPDATE ticket_type set type_name 'teenager' WHERE type_name = 'adult'

  -- 1) give discount, or change price? Change price.
  -- 2) allow to make more ticket types? Or, fixed to the 4 above? Allow.

  -- For consideration, should we allow the changing of prices,
  -- or allow a discount system. CREATE TABLE DISCOUNT( ... REFERENCE ticket_type(type_name)   );

  -- A discount system would mean that all four types will have fixed prices.
  -- then, the discount will modify the price value.
);

CREATE TABLE ticket
(
  id            SERIAL PRIMARY KEY,
  customer      Uuid                     NOT NULL REFERENCES loyalty_point (uuid),
  ticket_type   TEXT                     NOT NULL REFERENCES ticket_type (type_name),
  screening     INTEGER                  NOT NULL REFERENCES screening (id),
  seat          INTEGER                  NOT NULL REFERENCES seat (id),
  purchase_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

  -- Composite key
  -- Any entry should not have a duplicate combination of these two attributes.
  -- this.seat == that.seat && this.screening == that.screening { booked; }
  UNIQUE (seat, screening)
);

-- There are only five different food combos.
CREATE TABLE food_combo
(
  id          INTEGER PRIMARY KEY CHECK (id > 0 AND id <= 5),
  description TEXT           NOT NULL,                  -- e.g. "Popcorn and Coke"
  price       NUMERIC(10, 2) NOT NULL CHECK (price >= 0)-- e.g. 10.00
);

-- Frontend should handle ticket purchase,
-- send data to us,
-- then redirect customer to optional food_order purchase.
CREATE TABLE food_order
(
  id           SERIAL PRIMARY KEY,
  combo_number INTEGER NOT NULL REFERENCES food_combo (id),

  -- For consideration, no ideas for functionalities with order_time yet:
  ticket       INTEGER NOT NULL REFERENCES ticket (id)
  -- order_time   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
);

CREATE TABLE rating_review
(
  uuid   Uuid PRIMARY KEY REFERENCES loyalty_point (uuid),
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

-- Run another .sql file to populate the database with data below:


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
