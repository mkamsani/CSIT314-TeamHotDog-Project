DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA public;
-- Import extension for uuid_generate_v4()
-- Use gen_random_uuid() if not importing uuid-ossp.
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS pgcrypto;
CREATE EXTENSION IF NOT EXISTS citext;

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
  is_active      BOOL         NOT NULL DEFAULT TRUE, -- Suspends a movie.
  title          VARCHAR(255) NOT NULL,
  genre          Citext       NOT NULL, -- .toLowerCase() wikipedia
  description    VARCHAR(255) NOT NULL,
  release_date   DATE         NOT NULL,
  image_url      VARCHAR(255) NOT NULL DEFAULT 'https://upload.wikimedia.org/wikipedia/commons/thumb/d/d2/Popcorn_up_close_salted_and_air_popped.jpg/1280px-Popcorn_up_close_salted_and_air_popped.jpg',
  landscape_image_url         VARCHAR(255) NOT NULL DEFAULT 'https://upload.wikimedia.org/wikipedia/commons/thumb/d/d2/Popcorn_up_close_salted_and_air_popped.jpg/1280px-Popcorn_up_close_salted_and_air_popped.jpg',
  content_rating Citext NOT NULL,
  CHECK (content_rating IN ('g', 'pg', 'pg13', 'nc16', 'm18', 'r21'))
);

CREATE TABLE cinema_room
(
  PRIMARY KEY (id),
  id        INTEGER NOT NULL CHECK (id > 0 AND id <= 100),
  is_active BOOLEAN NOT NULL    DEFAULT TRUE             -- FALSE if room is under maintenance.
);

CREATE TABLE screening
(
  PRIMARY KEY (uuid),
  uuid        Uuid         NOT NULL DEFAULT    uuid_generate_v4(),
  status      VARCHAR(9)   NOT NULL DEFAULT    'active' CHECK (status IN ('active', 'suspended', 'cancelled')),
  movie_id    Uuid         NOT NULL REFERENCES movie (uuid),
  show_date   DATE         NOT NULL,
  show_time   VARCHAR(9)   NOT NULL CHECK (show_time IN ('morning', 'afternoon', 'evening', 'midnight')),
  cinema_room INTEGER      NOT NULL REFERENCES cinema_room (id)
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
  type_name  Citext         NOT NULL UNIQUE,                  -- Defaults: ('adult', 'student', 'child', 'senior')
  type_price NUMERIC(10, 2) NOT NULL CHECK (type_price >= 0), -- e.g. 10.50
  is_active  BOOLEAN        NOT NULL DEFAULT TRUE
);

CREATE TABLE ticket
(
  PRIMARY KEY (uuid),
  uuid          Uuid        NOT NULL DEFAULT    uuid_generate_v4(),
  customer      Uuid        NOT NULL REFERENCES user_account (uuid)     ON UPDATE CASCADE ON DELETE CASCADE,
  ticket_type   citext      NOT NULL REFERENCES ticket_type (type_name) ON UPDATE CASCADE ON DELETE CASCADE,
  screening     Uuid        NOT NULL REFERENCES screening (uuid)        ON UPDATE CASCADE ON DELETE CASCADE,
  seat          Uuid        NOT NULL REFERENCES seat (uuid),
  purchase_date Timestamptz NOT NULL DEFAULT    NOW(),

  -- Composite superkey
  -- this.seat == that.seat && this.screening == that.screening { "booked." }
  UNIQUE (seat, screening)
);

-- Rating review will be done with along with loyalty.
CREATE TABLE rating_review
(
  PRIMARY KEY (uuid),
  uuid         Uuid    NOT NULL DEFAULT uuid_generate_v4(),
  user_account Uuid    NOT NULL REFERENCES user_account (uuid) ON UPDATE CASCADE ON DELETE CASCADE,
  rating       INTEGER NOT NULL CHECK (rating > 0 AND rating <= 5),
  review       TEXT    NOT NULL CHECK (LENGTH(review) > 0),
  date_created DATE    NOT NULL DEFAULT NOW()
);

-- Used for JPA to map an entity to a view.
CREATE OR REPLACE VIEW revenue_report_base AS
SELECT t.purchase_date::DATE AS t_purchase_date,
       tt.type_name          AS t_type_name,
       tt.type_price         AS t_type_price,
       SUM(tt.type_price)    AS t_type_sum_revenue,
       COUNT(tt.type_name)   AS t_total_tickets
FROM ticket t
JOIN ticket_type tt ON t.ticket_type = tt.type_name
GROUP BY t.purchase_date, tt.type_price, tt.type_name;

-- Last month's revenue report.
CREATE OR REPLACE VIEW revenue_report_last_month AS
SELECT t.purchase_date::DATE AS t_purchase_date,
       tt.type_name          AS t_type_name,
       tt.type_price         AS t_type_price,
       SUM(tt.type_price)    AS t_type_sum_revenue,
       COUNT(tt.type_name)   AS t_total_tickets
FROM ticket t
JOIN ticket_type tt ON t.ticket_type = tt.type_name
WHERE t.purchase_date::DATE BETWEEN DATE_TRUNC('month', NOW()  - INTERVAL '1 month')
                            AND     DATE_TRUNC('month', NOW()) - INTERVAL '1 day'
GROUP BY t.purchase_date, tt.type_price, tt.type_name
UNION -- Get the total for last month.
SELECT (DATE_TRUNC('month', NOW()))::DATE AS t_purchase_date,
       'total'                            AS t_type_name,
       0                                  AS t_type_price,
       SUM(ticket_type.type_price)        AS t_type_sum_revenue,
       COUNT(ticket.ticket_type)          AS t_total_tickets
FROM ticket JOIN ticket_type ON ticket.ticket_type = ticket_type.type_name
WHERE purchase_date::DATE BETWEEN DATE_TRUNC('month', NOW()  - INTERVAL '1 month')
                          AND     DATE_TRUNC('month', NOW()) - INTERVAL '1 day'
ORDER BY t_purchase_date, t_type_name;

-- Last week's revenue report.
CREATE OR REPLACE VIEW revenue_report_last_week AS
SELECT t.purchase_date::DATE AS t_purchase_date,
       tt.type_name          AS t_type_name,
       tt.type_price         AS t_type_price,
       SUM(tt.type_price)    AS t_type_sum_revenue,
       COUNT(tt.type_name)   AS t_total_tickets
FROM ticket t
         JOIN ticket_type tt ON t.ticket_type = tt.type_name
WHERE t.purchase_date::DATE BETWEEN DATE_TRUNC('week', NOW()  - INTERVAL '1 week')
                            AND     DATE_TRUNC('week', NOW()) - INTERVAL '1 day'
GROUP BY t.purchase_date, tt.type_price, tt.type_name
UNION -- Get the total for last week.
SELECT (DATE_TRUNC('week', NOW()))::DATE                           AS t_purchase_date,
       'total'                                                     AS t_type_name,
       0                                                           AS t_type_price,
       SUM(ticket_type.type_price)                                 AS t_type_sum_revenue,
       COUNT(ticket.ticket_type)                                   AS t_total_tickets
FROM ticket JOIN ticket_type ON ticket.ticket_type = ticket_type.type_name
WHERE purchase_date::DATE BETWEEN DATE_TRUNC('week', NOW()  - INTERVAL '1 week')
                          AND     DATE_TRUNC('week', NOW()) - INTERVAL '1 day'
ORDER BY t_purchase_date, t_type_name;

-- Yesterday's revenue report.
CREATE OR REPLACE VIEW revenue_report_yesterday AS
SELECT t.purchase_date::DATE AS t_purchase_date,
       tt.type_name          AS t_type_name,
       tt.type_price         AS t_type_price,
       SUM(tt.type_price)    AS t_type_sum_revenue,
       COUNT(tt.type_name)   AS t_total_tickets
FROM ticket t
         JOIN ticket_type tt ON t.ticket_type = tt.type_name
WHERE t.purchase_date::DATE = DATE_TRUNC('day', NOW()  - INTERVAL '1 day')
GROUP BY t.purchase_date, tt.type_price, tt.type_name
UNION -- Get the total for yesterday.
SELECT (DATE_TRUNC('day', NOW()))::DATE                            AS t_purchase_date,
       'total'                                                     AS t_type_name,
       0                                                           AS t_type_price,
       SUM(ticket_type.type_price)                                 AS t_type_sum_revenue,
       COUNT(ticket.ticket_type)                                   AS t_total_tickets
FROM ticket JOIN ticket_type ON ticket.ticket_type = ticket_type.type_name
WHERE purchase_date::DATE = DATE_TRUNC('day', NOW()  - INTERVAL '1 day')
ORDER BY t_purchase_date, t_type_name;

CREATE OR REPLACE VIEW negative_rating_review_last_month AS
  SELECT username, rating, review
    FROM rating_review rr
    INNER JOIN user_account ua ON rr.uuid = ua.uuid
    WHERE rating < 3 AND rr.uuid IN (
        SELECT uuid
        FROM loyalty_point
        WHERE loyalty_point.uuid IN (
        SELECT uuid
        FROM ticket
        WHERE purchase_date::DATE BETWEEN DATE_TRUNC('month', NOW()  - INTERVAL '1 month')
                                    AND     DATE_TRUNC('month', NOW()) - INTERVAL '1 day'
        )
    );