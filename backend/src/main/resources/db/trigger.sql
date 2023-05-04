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