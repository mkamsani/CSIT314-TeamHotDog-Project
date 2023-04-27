/**
 * This file is the SSOT (Single Source of Truth) for the application.
 * It contains the tables for the database, to be converted to Java entities.
 *
 * Datatypes will conform to SQL standard, with the following exceptions:
 * -- For UUID, the UUID datatype is used instead of CHAR(36).
 * Reference:
 * https://www.postgresql.org/docs/current/datatype.html#DATATYPE-TABLE
 * http://www.contrib.andrew.cmu.edu/~shadow/sql/sql1992.txt (Ctrl + F "4.1  Data types")
 * https://www.depesz.com/2010/03/02/charx-vs-varcharx-vs-varchar-vs-text/
 *
 * For standardisation of date and time, TIMESTAMP WITH TIME ZONE is used for
 * for all date and time related fields. Where time is not required, the time
 * will be truncated in Java.
 * Reference:
 * https://www.baeldung.com/jpa-java-time
 * https://www.cockroachlabs.com/blog/time-data-types-postgresql/#conclusion
 *
 * All table names are in lowercase:     'ticket',      not 'Ticket'.
 * All table names are in snake_case:    'ticket_type', not 'ticketType'.
 * All table names are in singular form: 'ticket_type', not 'ticket_types'.
 * In Java, the table name will be converted to camelCase: 'ticketType'.
 * Reference:
 * https://www.oracle.com/java/technologies/javase/codeconventions-namingconventions.html
 * https://www.postgresql.org/docs/current/sql-syntax-lexical.html#SQL-SYNTAX-IDENTIFIERS
 *
 * All CHECK/UNIQUE constraints are also validated in Java.
 * This is to fulfil the requirement of the project,
 * which is to handle all logic in the backend.
 *
 * Certain CREATE TRIGGER and CREATE FUNCTION statements (or part of the statement) are temporary,
 * and will be removed once the Java code is implemented.
 * These statements will be marked with a 'TO-DO: Convert to Spring Boot'.
 * TO-DO will not contain a hyphen in the actual comments below.
 *
 * TABLE/VIEW             |  CRUD  |  Status       |  Notes
 * user_profile           |  CRUS  |  In progress  |
 * user_account           |  CRUS  |  In progress  |  Delete == is_active FALSE.
 * loyalty_point          |   RU   |  In progress  |  No user-interaction on update.

 * movie                  |  CRUD  |  Not started  |
 * ticket_type            |  CRUD  |  Not started  |  Create when db is initialised.
 * food_combo             |  CRUD  |  Not started  |

 * cinema_room            |   RUS  |  Not started  |  Create when db is initialised.
 * seat                   |   R    |  Not started  |  Create when db is initialised.

 * screening              |  CRUD  |  Not started  | Depends on movie, and cinema_room
 * ticket                 |  CRUD  |  Not started  | Depends on screening, seat
 * food_order             |  CRUD  |  Not started  | Depends on ticket
 * rating_review          |  CRUD  |  Not started  | Depends on user_account/loyalty_point

 * monthly_revenue_report |   R    |  Not started  |
 * monthly_rating_report  |   R    |  Not started  |
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
  /*
   * uuid_generate_v4() is indicated as a fallback.
   * In Java, UUID.randomUUID() is used instead.
   */
  uuid      Uuid PRIMARY KEY DEFAULT uuid_generate_v4(),

  /*
   * A user's privilege is immutable and determined by the user's title.
   * VARCHAR(8) is used as the maximum length of 'customer' is 8.
  */
  privilege VARCHAR(8)   NOT NULL CHECK (privilege IN ('customer', 'manager', 'owner', 'admin')),

  /*
   * These titles should not be for generalized names like 'staff' or 'employee'.
   * Use professional titles, like 'senior manager', 'senior admin', 'intern manager'.
   * However, a user admin can ignore these rules.
   */
  title     VARCHAR(255) NOT NULL UNIQUE,

  is_active BOOLEAN NOT NULL DEFAULT TRUE,

  /*
   * Frontend should capitalise what should be capitalised, if they want.
   */
  CHECK (privilege != 'customer' OR title = 'Customer')
);
-- The 'privilege' column is used in queries to filter user profiles.
CREATE INDEX ON user_profile (privilege);
-- The 'title' column is used in the creation of a new user_account.
CREATE INDEX ON user_profile (title);
-- Insert default user profiles.
INSERT INTO user_profile
  (privilege, title)
VALUES
    -- todo lowecase all
  ('customer', 'customer'),
  ('manager', 'Junior Manager'),
  ('manager', 'Senior Manager'),
  ('owner', 'Chief Financial Officer'),
  ('owner', 'Chief Executive Officer'),
  ('admin', 'Junior Admin'),
  ('admin', 'Senior Admin'),
  ('admin', 'Chief Information Officer');

CREATE TABLE user_account
(
  /*
   * The uuid column here is different from the uuid column in user_profile.
   * This uuid column will be used to identify each user.
   * Unlike a user's username and email, this uuid is immutable.
   */
  uuid            Uuid PRIMARY KEY                  DEFAULT uuid_generate_v4(),

  user_profile    Uuid                     NOT NULL REFERENCES user_profile (uuid) ON DELETE RESTRICT,

  -- ON DELETE RESTRICT          -  prevent it happens
  -- ON DELETE CASCADE           -  all the children die
  -- ON DELETE SET DEFAULT NULL  - all the children become NULL
  -- ON DELETE ???

  /*
   * A suspended account (FALSE) will not be able to login.
   * A user_account is suspended (instead of deleted) for the following reasons:
   * -- Preserve data integrity.
   * -- Prevent a user from creating a new account with the same username/email.
   * -- Existing account may have tickets or food orders associated with it.
   */
  is_active       BOOLEAN                           DEFAULT TRUE,

  /*
   * The pgcrypto extension is used to generate a password hash.
   * This column is not visible to ANY user.
   */
  password_hash   VARCHAR(72)              NOT NULL CHECK (LENGTH(password_hash) <= 72 ),

  -- The columns BELOW are visible to the user.

  /*
   * Username is used for login.
   * -- username is alphanumeric, hence '.' or '@' is invalid.
   * In Java, String.toLowerCase() is used to convert all usernames and emails to lowercase.
   */
  username        VARCHAR(255)             NOT NULL UNIQUE CHECK (username = LOWER(username)),
  email           VARCHAR(255)             NOT NULL UNIQUE CHECK (email = LOWER(email)),

  first_name      VARCHAR(255)             NOT NULL,
  last_name       VARCHAR(255)             NOT NULL,

  /*
   * No validation is done for address.
   * This allows users to enter their address in any format.
   * The tickets provided by the cinema are digital,
   * therefore an address is only required for billing.
   */
  address         VARCHAR(255)             NOT NULL,

  /*
   * Date from now minus date of birth to obtain age, for ticket types:
   * SELECT EXTRACT(YEAR FROM AGE(NOW(), date_of_birth)) FROM user_account;
   */
  date_of_birth   DATE                     NOT NULL,

  /*
   * A timestamp is used to record the date and time of user_account creation.
   * This value can be used for future extensions, such as long-term user benefits.
   * A timestamp is used to record the date and time of the user's last login.
   * This value can be used for future extensions, such as user inactivity.
   */
  time_created    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  time_last_login TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE loyalty_point
(
  /*
   * ON DELETE CASCADE:
   * If the user_account which this entry belongs to is deleted, delete this entry.
   * It does not delete all the entire loyalty_point table, or the entire user_account table.
   * If only this entry is deleted, the user_account is not deleted,
   * this will not happen as Java code will not implement it.
   */
  uuid            Uuid PRIMARY KEY REFERENCES user_account (uuid) ON DELETE CASCADE,

  points_redeemed INTEGER NOT NULL DEFAULT 0 CHECK (points_redeemed >= 0),
  points_total    INTEGER NOT NULL DEFAULT 0 CHECK (points_total >= 0)
);

/*
 * When a user_account is inserted into the database,
 * the password given will be hashed using the pgcrypto extension.
 * The username and email will be trimmed and converted to lowercase.
 * The first_name, last_name, and address will be trimmed.
 */
CREATE OR REPLACE FUNCTION ua_fn_crypt()
  RETURNS TRIGGER
  LANGUAGE plpgsql
AS
$$
BEGIN
  new.password_hash = crypt(new.password_hash, gen_salt('bf'));

  -- TODO: Convert to Spring Boot.
  new.username = LOWER(TRIM(new.username));
  new.email = LOWER(TRIM(new.email));
  new.first_name = TRIM(new.first_name);
  new.last_name = TRIM(new.last_name);
  new.address = TRIM(new.address);
  RETURN new;
END;
$$;
CREATE OR REPLACE TRIGGER ua_trigger_crypt
  BEFORE INSERT OR UPDATE
  ON user_account
  FOR EACH ROW
EXECUTE PROCEDURE ua_fn_crypt();

/*
 * When a user_account is inserted into the database,
 * A conditional is performed to check their user_profile's uuid.
 * If their user_profile's uuid belongs to the privilege 'customer' in user_profile,
 * a customer's loyalty_point will be created.
 */
CREATE OR REPLACE FUNCTION lp_fn_create()
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
CREATE OR REPLACE TRIGGER lp_trigger_create
  AFTER INSERT
  ON user_account
  FOR EACH ROW
EXECUTE PROCEDURE lp_fn_create();

-- Insert 7 default user accounts, for the personnel of the cinema.
INSERT INTO user_account
  (password_hash, username, email, first_name, last_name, address, date_of_birth, user_profile)
VALUES
  ('password_Mgr_is_mgrJ', 'jim        ', 'jhalpert@hotdogbuns.com        ', 'Jim         ', 'Halpert       ', '126 Kellum Court, Scranton, PA 18510                ', '1979-10-20', (SELECT uuid FROM user_profile WHERE title = 'Junior Manager')),
  ('password_Mgr_is_mgrS', 'mscott     ', 'mscott@hotdogbuns.com          ', 'Michael     ', 'Scott         ', '621 Court Kellum, Not Narcs, AP 01581               ', '1962-08-16', (SELECT uuid FROM user_profile WHERE title = 'Senior Manager')),
  ('password_Owr_is_%CFO', 'dwallace   ', 'dwallace@hotdogbuns.com        ', 'David       ', 'Wallace       ', '6818 Smith Lake, Schimmelland, RI 93473             ', '1965-02-13', (SELECT uuid FROM user_profile WHERE title = 'Chief Financial Officer')),
  ('password_Owr_is_%CEO', 'jbennett   ', 'jbennett@hotdogbuns.com        ', 'Joleen      ', 'Bennett       ', 'Suite 814 91331 Kristeen Flats, Juliohaven, FL 00750', '1948-06-28', (SELECT uuid FROM user_profile WHERE title = 'Chief Executive Officer')),
  ('password_Adm_is_admJ', 'marcus     ', 'marcus@adm.hotdogbuns.com      ', 'Marcus      ', 'Hutchins      ', '096 Jeannine Tunnel, East Alysia, NY 96753          ', '1994-01-01', (SELECT uuid FROM user_profile WHERE title = 'Junior Admin')),
  ('password_Adm_is_admS', 'samy       ', 'samy@adm.hotdogbuns.com        ', 'Samy        ', 'Kamkar        ', 'Apt. 209 29633 Gianna Parks, North Lonna, OK 27899  ', '1985-12-10', (SELECT uuid FROM user_profile WHERE title = 'Senior Admin')),
  ('password_Adm_is_%CIO', 'stonebraker', 'stonebraker@adm.hotdogbuns.com ', 'Michael     ', 'Stonebraker   ', 'Apt. 802 240 Freda Canyon, Connellytown, UT 62204   ', '1943-10-11', (SELECT uuid FROM user_profile WHERE title = 'Chief Information Officer'));
-- Insert 5 customers of the cinema:
-- TODO

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
  title          VARCHAR(255) NOT NULL,
  genre          VARCHAR(255) NOT NULL, -- .toLowerCase() wikipedia
  description    VARCHAR(255) NOT NULL, -- .toLowerCase() wikipedia
  release_date   DATE         NOT NULL, -- .toLowerCase() wikipedia
  image_url      VARCHAR(255) DEFAULT 'https://raw.githubusercontent.com/assets/default.jpg', -- .toLowerCase()
  content_rating VARCHAR(255) NOT NULL, -- .toLowerCase()
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
  id          SERIAL PRIMARY KEY,
  movie_id    INTEGER    NOT NULL REFERENCES movie (id),
  show_time   VARCHAR(9) NOT NULL CHECK (show_time IN ('morning', 'afternoon', 'evening', 'midnight')),
  cinema_room INTEGER    NOT NULL REFERENCES cinema_room (id),
  is_active BOOLEAN,

  /*
   * In Java, we filter dates that are lesser than today/now()
   * In Database, we do it as a trigger, that will set all dates
   * lesser than today to FALSE for "is_active"
   */
  date_of_movie TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

-- Pre-generated 280 seats, per cinema room.
CREATE TABLE seat
(
  id            SERIAL PRIMARY KEY,
  cinema_room   INTEGER      NOT NULL REFERENCES cinema_room (id) ON DELETE CASCADE,

  /*
   * A1, A2, A3, ..., A20, B1, B2, ..., B20, ..., N1, N2, ..., N20
   * Total: 20 * 14 = 280 seats.
   * Why stop at N? 'O' is easily mistaken for '0'.
   */
  seat_row      CHAR(1)      NOT NULL CHECK (seat_row >= 'A' AND seat_row <= 'N'),
  seat_column   INTEGER      NOT NULL CHECK (seat_column >= 1 AND seat_column <= 30),

  -- Additional enhancements, can remove if not implemented.
  seat_type     VARCHAR(255) NOT NULL CHECK (seat_type IN ('normal', 'wheelchair', 'disabled')),
  -- Additional enhancements, can remove if not implemented.
  seat_category VARCHAR(255) NOT NULL CHECK (seat_category IN ('normal', 'gold')),

  UNIQUE (cinema_room, seat_row, seat_column)
);

CREATE TABLE ticket_type
(
    uuid UUID default uuid_generate_v4(),
    -- pregenerate ('adult', 'student', 'child', 'senior')
  type_name    VARCHAR(7) UNIQUE,
  type_price   NUMERIC(10, 2)           NOT NULL CHECK (type_price >= 0), -- 10.50
  is_active BOOLEAN DEFAULT TRUE

-- why generate uuid or auto-increment primary key?
-- UPDATE ticket_type set type_name 'student' WHERE type_name = 'teenager'
-- UPDATE ticket_type set type_name 'adult' WHERE type_name = 'student'
-- UPDATE ticket_type set type_name 'teenager' WHERE type_name = 'adult'

-- 1) give discount, or change price? Change price.
-- 2) allow to make more ticket types? Or, fixed to the 4 above?

  -- For consideration, should we allow the changing of prices,
  -- or allow a discount system. CREATE TABLE DISCOUNT( ... REFERENCE ticket_type(type_name)   );

  -- A discount system would mean that all four types will have fixed prices.
  -- then, the discount will modify the price value.
);

CREATE TABLE ticket
(
  id            SERIAL PRIMARY KEY,
  customer      Uuid                     NOT NULL REFERENCES loyalty_point (uuid),
  ticket_type   VARCHAR(255)             NOT NULL REFERENCES ticket_type (type_name),
  screening INTEGER                  NOT NULL REFERENCES screening (id),
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
  description TEXT           NOT NULL, -- e.g. "Popcorn and Coke"
  price       NUMERIC(10, 2) NOT NULL  CHECK (price >= 0)-- e.g. 10.00
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
