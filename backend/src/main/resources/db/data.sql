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

INSERT INTO user_account
  (password_hash, username, email, first_name, last_name, address, date_of_birth, user_profile)
VALUES
  -- 7 personnel of the cinema:
  ('password_Mgr_is_mgrJ', 'jim', 'jhalpert@hotdogbuns.com', 'Jim', '   Halpert', '126 Kellum Court, Scranton, PA 18510', '1979-10-20', (SELECT uuid FROM user_profile WHERE title = 'junior manager')),
  ('password_Mgr_is_mgrS', 'mscott', 'mscott@hotdogbuns.com', 'Michael', 'Scott', '621 Court Kellum, Not Narcs, AP 01581', '1962-08-16', (SELECT uuid FROM user_profile WHERE title = 'senior manager')),
  ('password_Owr_is_%CFO', 'dwallace', 'dwallace@hotdogbuns.com', 'David', 'Wallace', '6818 Smith Lake, Schimmelland, RI 93473', '1965-02-13', (SELECT uuid FROM user_profile WHERE title = 'chief financial officer')),
  ('password_Owr_is_%CEO', 'jbennett', 'jbennett@hotdogbuns.com', 'Joleen', 'Bennett', 'Suite 814 91331 Kristeen Flats, Juliohaven, FL 00750', '1948-06-28', (SELECT uuid FROM user_profile WHERE title = 'chief executive officer')),
  ('password_Adm_is_admJ', 'marcus', 'marcus@adm.hotdogbuns.com', 'Marcus', 'Hutchins', '096 Jeannine Tunnel, East Alysia, NY 96753', '1994-01-01', (SELECT uuid FROM user_profile WHERE title = 'junior admin')),
  ('password_Adm_is_admS', 'samy', 'samy@adm.hotdogbuns.com', 'Samy', 'Kamkar', 'Apt. 209 29633 Gianna Parks, North Lonna, OK 27899', '1985-12-10', (SELECT uuid FROM user_profile WHERE title = 'senior admin')),
  ('password_Adm_is_%CIO', 'stonebraker', 'stonebraker@adm.hotdogbuns.com', 'Michael', 'Stonebraker', 'Apt. 802 240 Freda Canyon, Connellytown, UT 62204', '1943-10-11', (SELECT uuid FROM user_profile WHERE title = 'chief information officer')),
  -- 5 customers:
  ('password_0', 'user_0', 'logan.kling@yahoo.com', 'Alexis', 'Flatley', '452 Hung Junction, Armstrongshire, MN 95193', '2004-08-26', (SELECT uuid FROM user_profile WHERE title = 'customer')),
  ('password_1', 'user_1', 'lavinia.morissette@yahoo.com', 'Bernie', 'Pollich', 'Suite 723 591 Towne Greens, Larryshire, AL 50078', '1989-08-06', (SELECT uuid FROM user_profile WHERE title = 'customer')),
  ('password_2', 'user_2', 'dwain.franecki@yahoo.com', 'Sonya', 'Larson', '8788 Franecki Coves, Kenethton, OK 44141', '1990-10-15', (SELECT uuid FROM user_profile WHERE title = 'customer')),
  ('password_3', 'user_3', 'an.romaguera@hotmail.com', 'Ellan', 'Bashirian', 'Apt. 849 7652 Michaele Fields, New Keelymouth, VA 07784', '1974-04-02', (SELECT uuid FROM user_profile WHERE title = 'customer')),
  ('password_4', 'user_4', 'jarred.herzog@gmail.com', 'Orlando', 'Bradtke', '5259 Marshall Shoals, Priceburgh, OR 34597', '1996-05-29', (SELECT uuid FROM user_profile WHERE title = 'customer')),
  ('password_5', 'user_5', 'laree.kulas@yahoo.com', 'Olen', 'Legros', '4878 Santos Island, Gutmannside, AK 31521', '1998-03-16', (SELECT uuid FROM user_profile WHERE title = 'customer'));

-- I want to insert 5 dummy data to my movie table
INSERT INTO movie
  (title, genre, description, release_date, image_url, is_active, content_rating)
VALUES
  -- Use this website: https://www.themoviedb.org/?language=en-US to get the picture.
  -- I changed the URL for spider-man. It should be image.tmdb.org/t/p/original/...
  ('Spider-Man', 'action', 'Peter Parker, a high school student becomes a superhero, Spider Man.', '2002-05-03', 'https://image.tmdb.org/t/p/original/uJYYizSuA9Y3DCs0qS4qWvHfZg4.jpg', TRUE, 'pg13'),
  ('Batman Begins', 'action', 'Bruce Wayne, a billionare in Gotham City becomes a superhero, Bat Man', '2005-06-15', 'https://pt.wikipedia.org/wiki/Batman_Begins#/media/Ficheiro:Batman_begins.jpg', TRUE, 'pg13'),
  ('Ultraman', 'sci-fi', 'A giant alien warrior comes to Earth to fight off dinosaurs like T-Rex.', '1966-07-17', 'https://en.wikipedia.org/wiki/Ultraman_Nexus_%28character%29#/media/File:Ultraman_Nexus_Crunchyroll_Poster.jpg', TRUE, 'g'),
  ('Wonder Woman', 'action', 'An Amazon princess becomes Wonder Woman', '2017-06-02', 'https://en.wikipedia.org/wiki/Wonder_Woman_%282017_film%29#/media/File:Wonder_Woman_(2017_film)_poster.jpg', FALSE, 'pg13'),
  ('Matrix', 'action', 'A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers', '1999-03-31', 'https://en.wikipedia.org/wiki/The_Matrix#/media/File:The_Matrix_Poster.jpg', FALSE, 'r21'),
  ('The Shawshank Redemption', 'drama', 'Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency', '1994-09-23', 'https://en.wikipedia.org/wiki/The_Shawshank_Redemption#/media/File:ShawshankRedemptionMoviePoster.jpg', FALSE, 'r21'),
  ('Inception', 'action', 'A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a CEO', '2010-07-13', 'https://en.wikipedia.org/wiki/Inception#/media/File:Inception_(2010)_theatrical_poster.jpg', TRUE, 'm18'),
  ('Jurassic Park', 'adventure', 'During a preview tour, a theme park suffers a major power breakdown that allows its cloned dinosaur exhibits to run amok', '1993-06-09', 'https://en.wikipedia.org/wiki/Jurassic_Park_(film)#/media/File:Jurassic_Park_poster.jpg', TRUE, 'pg'),
  ('The Godfather', 'crime', 'The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son', '1972-03-24', 'https://en.wikipedia.org/wiki/The_Godfather#/media/File:Godfather-Part-1-Poster.jpg', FALSE, 'r21');

INSERT INTO cinema_room (id) VALUES (1), (2), (3), (4), (5), (6), (7), (8);

-- uuid
-- is_active
-- movie_id
-- cinema_room
-- show_date
-- show_time

-- Generate 20 random screenings please.
/*INSERT INTO screening
  (movie_id, cinema_room, show_date, show_time)
VALUES
  (1, 1, '2020-01-01', '10:00');*/


-- Insert default ticket types.
INSERT INTO ticket_type
  (type_name, type_price, is_active)
VALUES
  ('adult', 10.50, TRUE),
  ('child', 5.50, TRUE),
  ('senior', 6.50, TRUE),
  ('student', 8.50, TRUE),
  ('test', 10.00, TRUE);

SELECT 'Success' AS result;