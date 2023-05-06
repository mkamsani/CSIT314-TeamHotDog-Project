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
    yyyy integer;
    mm integer;
    dd integer;
BEGIN
    SELECT uuid FROM movie ORDER BY RANDOM() LIMIT 1 INTO random_movie;
    SELECT id FROM cinema_room ORDER BY RANDOM() LIMIT 1 INTO random_cinema_room;
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

-- Every time the screening table is updated or selected from,
-- check all the rows and set the screenings that are before NOW() to FALSE for the is_active column.
CREATE OR REPLACE FUNCTION screening_is_active()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    UPDATE screening
    SET is_active = FALSE
    WHERE show_date < NOW()::date
    OR (show_date = NOW()::date AND
        -- if it's 10 AM and show_time is 'afternoon', then it's still active.
        -- if it's  6 PM and show_time is 'afternoon', then it's not active.
        CASE WHEN show_time = 'morning'   THEN 1
             WHEN show_time = 'afternoon' THEN 2
             WHEN show_time = 'evening'   THEN 3
             ELSE 4
        END < CASE WHEN NOW()::TIME < '12:00:00' AND
                        NOW()::TIME > '10:00:00' THEN 1
                   WHEN NOW()::TIME < '17:00:00' THEN 2
                   WHEN NOW()::TIME < '22:00:00' THEN 3
                   ELSE 4
              END
       );
    RETURN new;
END;
$$;
CREATE OR REPLACE TRIGGER screening_is_active
AFTER UPDATE OR INSERT ON screening FOR EACH ROW
EXECUTE PROCEDURE screening_is_active();

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