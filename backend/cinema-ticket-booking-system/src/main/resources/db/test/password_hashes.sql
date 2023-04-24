CREATE EXTENSION IF NOT EXISTS pgcrypto;
-- 10 different hashes.
SELECT crypt('password', gen_salt('bf')) = '$2a$06$wIh2r.hXHEVaIBicp9uTJuNuWgYFwbXWTY.8X4w16n5lUQWYoGh5S'
FROM GENERATE_SERIES(1, 10);

SELECT gen_salt('bf');

-- true
SELECT '$2a$06$wIh2r.hXHEVaIBicp9uTJuNuWgYFwbXWTY.8X4w16n5lUQWYoGh5S' =
       crypt('password', '$2a$06$wIh2r.hXHEVaIBicp9uTJu') AS valid;

SELECT crypt('p', gen_salt('argon2i'));

SELECT *
FROM pg_catalog.pg_available_extensions;


SELECT password_hash FROM user_account WHERE username = 'stonebraker';

