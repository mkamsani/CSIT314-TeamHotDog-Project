/*
 * Hash the password of a new user_account,
 * or a user_account with updated password.
 * Prerequisites: user_profile and user_account TABLE must be created.
 */
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
BEFORE INSERT OR UPDATE ON user_account FOR EACH ROW
EXECUTE PROCEDURE password_crypt();

/*
 * If a user_account has the privilege 'customer' in user_profile,
 * A customer's loyalty_point will be created.
 * Prerequisites: user_profile, user_account, loyalty_point TABLE must be created.
 */
CREATE OR REPLACE FUNCTION loyalty_point_create()
RETURNS TRIGGER LANGUAGE plpgsql
AS
$$
BEGIN
    IF (SELECT privilege FROM user_profile WHERE uuid = new.user_profile) = 'customer' THEN
        INSERT INTO loyalty_point (uuid) VALUES (new.uuid);
    END IF;
    RETURN new;
END;
$$;
CREATE OR REPLACE TRIGGER loyalty_point_create
AFTER INSERT ON user_account FOR EACH ROW
EXECUTE PROCEDURE loyalty_point_create();

/*
 * Creates a new screening with random movie, cinema_room, show_date and show_time.
 * Prerequisites: movie, cinema_room, screening TABLE must be created.
 */
CREATE OR REPLACE PROCEDURE random_screening()
    LANGUAGE plpgsql
AS $$
DECLARE
    random_movie uuid;
    random_cinema_room integer;
    random_show_date date;
    random_show_time varchar(20);
    screening varchar(9);
    yyyy integer;
    mm integer;
    dd integer;
BEGIN
    SELECT uuid FROM movie ORDER BY RANDOM() LIMIT 1 INTO random_movie;
    SELECT id FROM cinema_room ORDER BY RANDOM() LIMIT 1 INTO random_cinema_room;
    screening  := 'active';
    yyyy := 2023;
    mm := CEIL(RANDOM() * 12);
    dd := CEIL(RANDOM() * 31);
    dd := CASE
          WHEN mm = 2 AND dd > 28 THEN 28
          WHEN mm IN (4, 6, 9, 11) AND dd > 30 THEN 30
          ELSE dd
          END;
    random_show_date := yyyy || '-' || mm || '-' || dd;
    SELECT CASE WHEN RANDOM() < .25 THEN 'morning'
                WHEN RANDOM() < .5  THEN 'afternoon'
                WHEN RANDOM() < .75 THEN 'evening'
                ELSE 'midnight'
                END INTO random_show_time;
    -- Abort the procedure if the screening already exists.
    -- A screening exists if it has the same cinema_room, show_date, show_time.
    IF EXISTS (SELECT * FROM screening
               WHERE cinema_room = random_cinema_room
                 AND show_date = random_show_date
                 AND show_time = random_show_time) THEN
        RAISE NOTICE 'Screening already exists: %, %, %',
                     random_cinema_room, random_show_date, random_show_time;
    ELSE
        INSERT INTO screening
            (movie_id, cinema_room, show_date, show_time)
        VALUES
            (random_movie, random_cinema_room, random_show_date, random_show_time);
    END IF;
END;
$$;

/*
 * Do an insert statement of 280 seats.
 * Use series of loops to insert 14 rows of 20 seats.
 * Prerequisites: cinema_room and seat TABLE must be created.
 */
CREATE OR REPLACE FUNCTION seat_create()
RETURNS TRIGGER LANGUAGE plpgsql
AS
$$
BEGIN
    FOR i IN 1..14 LOOP
        FOR j IN 1..20 LOOP
            INSERT INTO seat
                (cinema_room, seat_row, seat_column)
            VALUES
                (new.id, CHR(64 + i), j);
            END LOOP;
        END LOOP;
    RETURN new;
END;
$$;
CREATE OR REPLACE TRIGGER seat_create
AFTER INSERT ON cinema_room FOR EACH ROW
EXECUTE PROCEDURE seat_create();

/*
 * Create a new ticket with random screening, seat, and user_account.
 * Prerequisites: screening, seat, user_account, ticket TABLE must be created.
 * new ticket should not clash with existing ticket
 */

CREATE OR REPLACE PROCEDURE random_ticket()
    LANGUAGE plpgsql
AS $$
DECLARE
    random_screening uuid;
    random_seat uuid;
    random_user_account uuid;
    random_ticket_type varchar(20);
    random_purchase_date date;
    yyyy integer;
    mm integer;
    dd integer;
BEGIN
    select uuid from screening order by random() limit 1 into random_screening;
    select uuid from seat order by random() limit 1 into random_seat;
    select uuid from user_account
                where user_profile = (SELECT uuid FROM user_profile WHERE privilege = 'customer')
                order by random() limit 1 into random_user_account;
    select case when random() < .25 then 'adult'
                when random() < .5 then 'child'
                when random() < .75 then 'senior'
                else 'student'
                end into random_ticket_type;
    yyyy := 2023;
    mm := CEIL(RANDOM() * 12);
    dd := CEIL(RANDOM() * 31);
    dd := CASE
        WHEN mm = 2 AND dd > 28 THEN 28
        WHEN mm IN (4, 6, 9, 11) AND dd > 30 THEN 30
        ELSE dd
        END;
    random_purchase_date := yyyy || '-' || mm || '-' || dd;
    -- Abort a procedure if the seat is already taken
    -- A seat is taken if it has the same screening and seat.
    IF EXISTS (SELECT * FROM ticket
               WHERE screening = random_screening
                 AND seat = random_seat) THEN
        RAISE NOTICE 'Seat is already taken: %, %',
                     random_screening, random_seat;
    ELSE
        INSERT INTO ticket
            (customer, ticket_type, screening, seat, purchase_date)
        VALUES
            (random_user_account, random_ticket_type, random_screening, random_seat, random_purchase_date);
    END IF;
END;
$$;

CREATE OR REPLACE FUNCTION loyalty_point_increaase()
RETURNS TRIGGER LANGUAGE plpgsql
AS
$$
BEGIN
    UPDATE loyalty_point
    SET points_total = points_total + 1
    WHERE loyalty_point.uuid = new.customer;
    RETURN new;
END;
$$;
CREATE OR REPLACE TRIGGER loyalty_point_increaase
AFTER INSERT ON ticket FOR EACH ROW
EXECUTE PROCEDURE loyalty_point_increaase();
