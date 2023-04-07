DROP TABLE IF EXISTS user_account;
-- Import extension for uuid_generate_v4()
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE user_account
(
    --  Not visible to user:
    numeric_id      SMALLSERIAL PRIMARY KEY,
    id              Uuid         NOT NULL UNIQUE DEFAULT uuid_generate_v4(),
    username        VARCHAR(255) NOT NULL UNIQUE,
    --  TODO see 'role' and 'type' as variable name
    account_type    VARCHAR(255) NOT NULL        DEFAULT 'customer',
    password_hash   VARCHAR(255) NOT NULL,

    --  Visible to user:
    first_name      VARCHAR(255) NOT NULL,
    last_name       VARCHAR(255) NOT NULL,
    email           VARCHAR(255) NOT NULL UNIQUE,
    address         VARCHAR(255) NOT NULL,
    time_created    TIMESTAMP    NOT NULL        DEFAULT NOW(),
    time_last_login TIMESTAMP    NOT NULL        DEFAULT NOW(),

    --  Constraints:
    CONSTRAINT enum_account_type CHECK (account_type IN ('customer', 'manager', 'owner', 'admin')),
);

-- Trigger to lowercase username.
CREATE OR REPLACE FUNCTION f_lower() RETURNS TRIGGER AS
$$
BEGIN
    new.username = LOWER(new.username);
    new.email = LOWER(new.email);
    RETURN new;
END;
$$ LANGUAGE plpgsql;
CREATE OR REPLACE TRIGGER t_lowercase_username
    BEFORE INSERT
    ON user_account
    FOR EACH ROW
EXECUTE FUNCTION f_lower();

INSERT INTO user_account (username, password_hash, first_name, last_name, email, address)
VALUES ('dummyOne', 'summer2023', 'John', 'Doe', 'johndoe@hotdog.com', '13 Avenue, Singapore, 123456');

INSERT INTO user_account (username, password_hash, first_name, last_name, email, address)
VALUES ('dummyTwo', 'red-house', 'Jane', 'Smith', 'jane.smith@hotdog.com', '14 Drive, Singapore, 678901');

SELECT * FROM user_account;