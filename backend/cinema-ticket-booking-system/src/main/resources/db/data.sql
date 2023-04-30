-- Insert default user profiles.
INSERT INTO user_profile
  (privilege, title)
VALUES
  ('customer', 'customer'),
  ('manager', 'junior manager'),
  ('manager', 'senior manager'),
  ('owner', 'chief financial officer'),
  ('owner', 'chief executive officer'),
  ('admin', 'junior admin'),
  ('admin', 'senior admin'),
  ('admin', 'chief information officer');

-- Insert 7 default user accounts, for the personnel of the cinema.
INSERT INTO user_account
  (password_hash, username, email, first_name, last_name, address, date_of_birth, user_profile)
VALUES
  ('password_Mgr_is_mgrJ', 'jim        ', 'jhalpert@hotdogbuns.com        ', 'Jim         ', 'Halpert       ', '126 Kellum Court, Scranton, PA 18510                ', '1979-10-20', (SELECT uuid FROM user_profile WHERE title = 'junior manager')),
  ('password_Mgr_is_mgrS', 'mscott     ', 'mscott@hotdogbuns.com          ', 'Michael     ', 'Scott         ', '621 Court Kellum, Not Narcs, AP 01581               ', '1962-08-16', (SELECT uuid FROM user_profile WHERE title = 'senior manager')),
  ('password_Owr_is_%CFO', 'dwallace   ', 'dwallace@hotdogbuns.com        ', 'David       ', 'Wallace       ', '6818 Smith Lake, Schimmelland, RI 93473             ', '1965-02-13', (SELECT uuid FROM user_profile WHERE title = 'chief financial officer')),
  ('password_Owr_is_%CEO', 'jbennett   ', 'jbennett@hotdogbuns.com        ', 'Joleen      ', 'Bennett       ', 'Suite 814 91331 Kristeen Flats, Juliohaven, FL 00750', '1948-06-28', (SELECT uuid FROM user_profile WHERE title = 'chief executive officer')),
  ('password_Adm_is_admJ', 'marcus     ', 'marcus@adm.hotdogbuns.com      ', 'Marcus      ', 'Hutchins      ', '096 Jeannine Tunnel, East Alysia, NY 96753          ', '1994-01-01', (SELECT uuid FROM user_profile WHERE title = 'junior admin')),
  ('password_Adm_is_admS', 'samy       ', 'samy@adm.hotdogbuns.com        ', 'Samy        ', 'Kamkar        ', 'Apt. 209 29633 Gianna Parks, North Lonna, OK 27899  ', '1985-12-10', (SELECT uuid FROM user_profile WHERE title = 'senior admin')),
  ('password_Adm_is_%CIO', 'stonebraker', 'stonebraker@adm.hotdogbuns.com ', 'Michael     ', 'Stonebraker   ', 'Apt. 802 240 Freda Canyon, Connellytown, UT 62204   ', '1943-10-11', (SELECT uuid FROM user_profile WHERE title = 'chief information officer'));
-- Insert 5 customers of the cinema:
-- TODO

-- i want to insert 5 dummy data to my movie table
--
INSERT INTO movie
  (title, genre, description, release_date, image_url, content_rating)
VALUES
    ('Spider-Man', 'Action', 'Peter Parker, a high school student becomes a superhero, Spider Man.', '2002-05-03', 'https://en.wikipedia.org/wiki/Spider-Man_%282002_film%29#/media/File:Spider-Man2002Poster.jpg', 'pg13'),
    ('Batman Begins', 'Action', 'Bruce Wayne, a billionare in Gotham City becomes a superhero, Bat Man', '2005-06-15', 'https://pt.wikipedia.org/wiki/Batman_Begins#/media/Ficheiro:Batman_begins.jpg', 'pg13'),
    ('Ultraman', 'Sci-Fi', 'A giant alien warrior comes to Earth to fight off dinosaurs like T-Rex.', '1966-07-17', 'https://en.wikipedia.org/wiki/Ultraman_Nexus_%28character%29#/media/File:Ultraman_Nexus_Crunchyroll_Poster.jpg', 'g'),
    ('Wonder Woman', 'Action', 'An Amazon princess becomes Wonder Woman', '2017-06-02', 'https://en.wikipedia.org/wiki/Wonder_Woman_%282017_film%29#/media/File:Wonder_Woman_(2017_film)_poster.jpg', 'pg13');



SELECT 'Success'