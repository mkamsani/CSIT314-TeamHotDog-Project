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
    SELECT uuid FROM movie ORDER BY random() LIMIT 1 INTO random_movie;
    SELECT id FROM cinema_room ORDER BY random() LIMIT 1 INTO random_cinema_room;
    yyyy := 2023;
    mm := ceil(random() * 12);
    dd := ceil(random() * 31);
    dd := CASE
          WHEN mm = 2 AND dd > 28 THEN 28
          WHEN mm IN (4, 6, 9, 11) AND dd > 30 THEN 30
          ELSE dd
          END;
    random_show_date := yyyy || '-' || mm || '-' || dd;
    SELECT CASE
               WHEN random() < 0.25 THEN 'morning'
               WHEN random() < 0.5 THEN 'afternoon'
               WHEN random() < 0.75 THEN 'evening'
               ELSE 'midnight'
               END INTO random_show_time;
    INSERT INTO screening
        (movie_id, cinema_room, show_date, show_time)
    VALUES
        (random_movie, random_cinema_room, random_show_date, random_show_time);
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
                (new.id, chr(64 + i), j);
            END LOOP;
        END LOOP;
    RETURN new;
END;
$$;
CREATE OR REPLACE TRIGGER seat_create
AFTER INSERT ON cinema_room FOR EACH ROW
EXECUTE PROCEDURE seat_create();