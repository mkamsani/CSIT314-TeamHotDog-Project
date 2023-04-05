create table movie
(
    id           serial primary key,
    title        varchar(255)   not null,
    description  varchar(255)   not null,
    release_date date           not null,
    rating       varchar(255)   not null,
    price        numeric(10, 2) not null,
    image_url    varchar(255)   not null
);

create table movie_session
(
    id        serial primary key,
    movie_id  int       not null,
    show_time timestamp not null,
    CONSTRAINT fk_movie_id FOREIGN KEY (movie_id) REFERENCES movie (id)
);

create table seat
(
    id     serial primary key,
    row    int not null,
    number int not null,
    CONSTRAINT seat_unique UNIQUE (row, number)
);

create table loyalty_points
(
    id      serial primary key,
    user_id int not null,
    points  int not null,
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES user_account (id)
);