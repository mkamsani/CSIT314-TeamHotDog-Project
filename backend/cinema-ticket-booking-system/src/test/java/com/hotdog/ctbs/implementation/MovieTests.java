package com.hotdog.ctbs.implementation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.hotdog.ctbs.service.implementation.*;
import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest
public class MovieTests {

    @Autowired
    private MovieImpl movieImpl;

    @Test
    void getter()
    {

        System.out.println("movieImpl.getAllTitles()");
        System.out.println(movieImpl.getAllTitles());
        System.out.println();

        System.out.println("movieImpl.getAllImages()");
        System.out.println(movieImpl.getAllImages());
        System.out.println();

        System.out.println("movieImpl.getMovieByTitle(\"Spider-Man\")");
        System.out.println(movieImpl.getMovieByTitle("Spider-Man"));
        System.out.println();

        System.out.println("movieImpl.getMovieById(UUID.fromString(\"a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11\"))");
        System.out.println(movieImpl.getMovieById(UUID.fromString("eb882ac5-83d5-442d-a570-a684adaf8c75")));
        System.out.println();

        System.out.println("movieImpl.getIdByTitle(\"Spider-Man\")");
        System.out.println(movieImpl.getIdByTitle("Spider-Man"));
        System.out.println();

        System.out.println("movieImpl.getAllContentRating()");
        System.out.println(movieImpl.getAllContentRating());
        System.out.println();

        System.out.println("movieImpl.getValidContentRating()");
        System.out.println(movieImpl.getValidContentRating());
        System.out.println();

    }

    @Test
    void createMethod()
    {
        // test create movie method
        System.out.println("movieImpl.createMovie(\"Pokemon Diamond and Pearl\", " +
                        "\"Adventurous\", \"A teenager dreams to become the Pokemon Master\", " +
                        "LocalDate.of(1997, 5, 3), " +
                        "\"https://www.imdb.com/title/tt0145487/mediaviewer/rm4039471360/\", " +
                        "\"PG13\")");
        movieImpl.createMovie(
                "Pokemon Diamond and Pearl", "Adventurous",
                "A teenager dreams to become the Pokemon Master",
                LocalDate.of(1997, 5, 3),
                "https://www.imdb.com/title/tt0145487/mediaviewer/rm4039471360/",
                "pg13");
        System.out.println();
        System.out.println(movieImpl.getAllTitles());
    }

    @Test
    void updateMethod()
    {
        // test update movie method
        System.out.println("The existing movie titles before an update made");
        System.out.println(movieImpl.getAllTitles());
        System.out.println("movieImpl.updateOneMovieByTitle(\"PowerPuffGirl\", \"Wonder Woman\")");
        movieImpl.updateOneMovieByTitle("PowerPuffGirl", "Wonder Woman");
        System.out.println();
        System.out.println(movieImpl.getAllTitles());
    }



}
