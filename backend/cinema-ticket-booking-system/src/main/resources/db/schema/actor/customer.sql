CREATE TABLE customer
(
    id             Uuid        NOT NULL PRIMARY KEY,
    customer_id    SMALLSERIAL NOT NULL,
    -- TRUE if customer is active, FALSE if customer is deactivated.
    account_status BOOLEAN     NOT NULL DEFAULT TRUE,
    FOREIGN KEY (id) REFERENCES user_account (id) ON DELETE CASCADE
);

CREATE TABLE child
(
    id        Uuid NOT NULL PRIMARY KEY,
    parent_id Uuid NOT NULL PRIMARY KEY,
    age       INTEGER,
    FOREIGN KEY (id) REFERENCES customer (id) ON DELETE CASCADE,
    FOREIGN KEY (parent_id) REFERENCES customer (id) ON DELETE CASCADE
);

CREATE TABLE adult
(
    id  Uuid NOT NULL PRIMARY KEY,
    age INTEGER,
    FOREIGN KEY (id) REFERENCES customer (id) ON DELETE CASCADE
);

CREATE TABLE elderly
(
    id  Uuid NOT NULL PRIMARY KEY,
    age INTEGER,
    FOREIGN KEY (id) REFERENCES customer (id) ON DELETE CASCADE
);