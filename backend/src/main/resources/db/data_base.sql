-- Insert default user profiles.
INSERT INTO user_profile
  (privilege, title)
VALUES
  ('customer', 'customer'),
  ('manager',  'junior manager'),
  ('manager',  'senior manager'),
  ('owner',    'chief financial officer'),
  ('owner',    'chief executive officer'),
  ('admin',    'junior admin'),
  ('admin',    'senior admin'),
  ('admin',    'chief information officer');

-- Insert default users accounts: 2 managers, 2 owners, 3 admins, 1 customer.
-- Use data_many.sql to insert more users.
INSERT INTO user_account
  (password_hash, username, email, first_name, last_name, address, date_of_birth, user_profile)
VALUES
  ('password-employee', 'jim',         'jhalpert@hotdogbuns.com',        'Jim',     'Halpert',     '126 Kellum Court, Scranton, PA 18510',                 '1979-10-20', (SELECT uuid FROM user_profile WHERE title = 'junior manager')),
  ('password-employee', 'mscott',      'mscott@hotdogbuns.com',          'Michael', 'Scott',       '621 Court Kellum, Not Narcs, AP 01581',                '1962-08-16', (SELECT uuid FROM user_profile WHERE title = 'senior manager')),
  ('password-employee', 'dwallace',    'dwallace@hotdogbuns.com',        'David',   'Wallace',     '6818 Smith Lake, Schimmelland, RI 93473',              '1965-02-13', (SELECT uuid FROM user_profile WHERE title = 'chief financial officer')),
  ('password-employee', 'jbennett',    'jbennett@hotdogbuns.com',        'Joleen',  'Bennett',     'Suite 814 91331 Kristeen Flats, Juliohaven, FL 00750', '1948-06-28', (SELECT uuid FROM user_profile WHERE title = 'chief executive officer')),
  ('password-employee', 'marcus',      'marcus@adm.hotdogbuns.com',      'Marcus',  'Hutchins',    '096 Jeannine Tunnel, East Alysia, NY 96753',           '1994-01-01', (SELECT uuid FROM user_profile WHERE title = 'junior admin')),
  ('password-employee', 'samy',        'samy@adm.hotdogbuns.com',        'Samy',    'Kamkar',      'Apt. 209 29633 Gianna Parks, North Lonna, OK 27899',   '1985-12-10', (SELECT uuid FROM user_profile WHERE title = 'senior admin')),
  ('password-employee', 'stonebraker', 'stonebraker@adm.hotdogbuns.com', 'Michael', 'Stonebraker', 'Apt. 802 240 Freda Canyon, Connellytown, UT 62204',    '1943-10-11', (SELECT uuid FROM user_profile WHERE title = 'chief information officer')),
  ('password-customer', 'customer0',   'im.king@example.com',            'King',    'Charles',     '4878 Santos Island, Gutmannside, AK 31521',            '1998-03-16', (SELECT uuid FROM user_profile WHERE title = 'customer'));

-- Insert 9 default movies.
INSERT INTO movie
  (title, genre, description, release_date, image_url, landscape_image_url,is_active, content_rating)
VALUES
  ('Spider-Man',               'action',    'Spider-Man follows the story of Peter Parker (Tobey Maguire), a high school student who gains spider-like ' ||
                                            'abilities and transforms into the superhero Spider-Man. He battles the Green Goblin (Willem Dafoe) to save New York City.',                                                               '2002-05-03', 'https://www.themoviedb.org/t/p/original/gh4cZbhZxyTbgxQPxD0dOudNPTn.jpg','https://www.themoviedb.org/t/p/original/gkINAPOuwUFo2Qphs3OUUbjUKUZ.jpg' , TRUE,  'pg13'),
  ('Batman Begins',            'action',    'Batman Begins stars Christian Bale as Bruce Wayne, who travels the world to ' ||
                                            'learn combat and stealth techniques to become Batman, aiming to rid Gotham City of corruption and fear.',                                               '2005-06-15', 'https://image.tmdb.org/t/p/original/8RW2runSEc34IwKN2D1aPcJd2UL.jpg', 'https://www.themoviedb.org/t/p/original/lh5lbisD4oDbEKgUxoRaZU8HVrk.jpg', TRUE,  'pg13'),
  ('Wonder Woman',             'action',    'Wonder Woman stars Gal Gadot as Diana, a trained Amazonian warrior who leaves her island home to fight' ||
                                            ' in World War I, discovering her full powers and true destiny as Wonder Woman along the way.',                                                          '2017-06-02', 'https://image.tmdb.org/t/p/original/imekS7f1OuHyUP2LAiTEM0zBzUz.jpg', 'https://www.themoviedb.org/t/p/original/bEFEIUJySSIjwPKWsh5MZjDLy63.jpg', TRUE, 'pg13'),
  ('Matrix',                   'action',    'The Matrix stars Keanu Reeves as Thomas Anderson, a computer programmer who discovers the world he knows is ' ||
                                            'a simulated reality, leading him to join a rebellion against the machines and their oppressive system.',                                                '1999-03-31', 'https://image.tmdb.org/t/p/original/sRaupdJawe6UTS0t77vwJoLjd7h.jpg', 'https://www.themoviedb.org/t/p/original/l4QHerTSbMI7qgvasqxP36pqjN6.jpg', TRUE, 'r21'),
  ('Inception',                'action',    'Leonardo DiCaprio stars as Dom Cobb, an expert thief who specializes in infiltrating people''s dreams to steal ' ||
                                            'valuable information.',                                                                                                                                 '2010-07-13', 'https://image.tmdb.org/t/p/original/8IB2e4r4oVhHnANbnm7O3Tj6tF8.jpg', 'https://www.themoviedb.org/t/p/original/s3TBrRGB1iav7gFOCNx3H31MoES.jpg', TRUE,  'm18'),
  ('Your Name',                'fantasy',   'Your Name is a romantic anime film directed by Makoto Shinkai. It follows two teenagers, Taki (Ryunosuke Kamiki) ' ||
                                            'and Mitsuha (Mone Kamishiraishi), who mysteriously swap bodies and try to find each other.',                                                            '2016-08-26', 'https://www.themoviedb.org/t/p/original/f7UABkINaQCWMHUOkq1XdB7SFOS.jpg', 'https://www.themoviedb.org/t/p/original/3VIRYQTEC6pZSv3kUE5yPGVVg0i.jpg', TRUE, 'pg'),
  ('Black Adam',               'action',    'Starring Dwayne Johnson, Black Adam is a superhero film that chronicles the transformation of a once ' ||
                                            'anti-heroic character into a full-fledged hero, based on the DC Comics character of the same name.',                                                    '2022-07-29', 'https://www.themoviedb.org/t/p/original/3zXceNTtyj5FLjwQXuPvLYK5YYL.jpg', 'https://www.themoviedb.org/t/p/original/jVsbzy5gj3McD8V6dDr7EMrLSqT.jpg', TRUE, 'pg13'),
  ('Avatar',                   'adventure', 'Avatar is a sci-fi epic directed by James Cameron and stars Sam Worthington as Jake Sully, a paralyzed marine who is ' ||
                                            'sent to the alien world of Pandora and becomes involved in a conflict between humans and the planet''s indigenous people, the Na vi.',                  '2009-12-18', 'https://www.themoviedb.org/t/p/original/bUlqqicJFO7WEhTigb3EJCJvjzy.jpg', 'https://www.themoviedb.org/t/p/original/7ABsaBkO1jA2psC8Hy4IDhkID4h.jpg', TRUE, 'pg13'),
  ('I am Number Four',         'sci-fi',    'Starring Alex Pettyfer, "I am Number Four" is a science fiction action film about a teenager named John Smith, ' ||
                                            'who possesses extraordinary powers and must evade those who seek to harm him.',                                                                         '2011-02-18', 'https://www.themoviedb.org/t/p/original/70qx2gTHvmqtKkJb56dNbVW2aB7.jpg', 'https://www.themoviedb.org/t/p/original/lR8eHumEoL8FkszpDvbEUZRXVt5.jpg', TRUE, 'pg13'),
  ('Avengers: Infinity War',   'adventure', 'The superhero blockbuster "Avengers: Infinity War" features Robert Downey Jr., Chris Hemsworth, and Chris Evans as the Avengers, ' ||
                                            'who fight against Thanos to prevent the destruction of the universe.',                                                                                  '2018-04-27', 'https://www.themoviedb.org/t/p/original/rcV5bQjDoPNfI4wRpWAgZXtE0ON.jpg', 'https://www.themoviedb.org/t/p/original/lmZFxXgJE3vgrciwuDib0N8CfQo.jpg', TRUE, 'pg13');

-- Insert 8 default cinema rooms.
-- No further cinema rooms can be added.
INSERT INTO cinema_room (id) VALUES (1), (2), (3), (4), (5), (6), (7), (8);

-- Insert 5 default screenings, with a fixed date and time.
INSERT INTO screening
    (movie_id, cinema_room, show_date, show_time, is_active)
VALUES
    ((SELECT uuid FROM movie WHERE title = 'Spider-Man'), 1, '2023-05-23', 'morning',   TRUE),
    ((SELECT uuid FROM movie WHERE title = 'Spider-Man'), 1, '2023-05-23', 'afternoon', TRUE),
    ((SELECT uuid FROM movie WHERE title = 'Spider-Man'), 1, '2023-05-23', 'evening',   TRUE),
    ((SELECT uuid FROM movie WHERE title = 'Spider-Man'), 1, '2023-05-23', 'midnight',  TRUE),
    ((SELECT uuid FROM movie WHERE title = 'Spider-Man'), 5, '2023-01-01', 'afternoon', FALSE);
-- Insert a maximum of 495 random screenings.
DO $$
  DECLARE
  BEGIN
    WHILE (SELECT COUNT(*) FROM screening) < 495 LOOP
      CALL random_screening();
    END LOOP;
  END
$$;

-- Insert default ticket types.
INSERT INTO ticket_type
  (type_name, type_price, is_active)
VALUES
  ('adult',   10.50, TRUE),
  ('child',   5.50,  TRUE),
  ('senior',  6.50,  TRUE),
  ('student', 8.50,  TRUE),
  ('test',    10.00, TRUE);

-- Development views, to present data in a more readable format.
CREATE OR REPLACE VIEW dev_screening_view AS
SELECT TO_CHAR(show_date, 'Mon DD') AS show_date, cinema_room, show_time, title
FROM screening
INNER JOIN movie ON movie.uuid = screening.movie_id
WHERE screening.is_active = TRUE
ORDER BY show_date, cinema_room, show_time, title;

CREATE OR REPLACE VIEW dev_cinema_room_view AS
SELECT id, is_active, COUNT(seat.*) AS total_seats
FROM cinema_room
INNER JOIN seat ON cinema_room.id = seat.cinema_room
GROUP BY is_active, id
ORDER BY id;

CREATE OR REPLACE VIEW dev_user_account_view AS
SELECT username, email, title, privilege,
       user_account.is_active active_account,
       user_profile.is_active active_title,
       CONCAT(user_account.first_name, ' ', user_account.last_name) full_name,
       TO_CHAR(user_account.time_created,    'DD Mon YYYY HH24:MI:SS') created_at,
       TO_CHAR(user_account.time_last_login, 'DD Mon YYYY HH24:MI:SS') last_login_at
FROM user_account
INNER JOIN user_profile ON user_profile.uuid = user_account.user_profile
ORDER BY user_account.username;

CREATE OR REPLACE VIEW dev_ticket_view AS
SELECT ticket.uuid, username, screening.cinema_room, show_date, show_time, concat(seat_row, seat_column) seat_concat,
       type_name t_type, type_price price
FROM ticket
         INNER JOIN user_account ON user_account.uuid = ticket.customer
         INNER JOIN ticket_type ON ticket_type.type_name = ticket.ticket_type
         INNER JOIN seat ON seat.uuid = ticket.seat
         INNER JOIN screening ON screening.uuid = ticket.screening;

SELECT 'Success' AS result;