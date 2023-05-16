package com.hotdog.ctbs.implementation;

import com.hotdog.ctbs.service.implementation.MovieImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class MovieTests {

    @Autowired
    private MovieImpl movieImpl;

    @Test
    void getter()
    {

        // test getter methods
        System.out.println("movieImpl.getAllMovieTitles()");
        System.out.println(movieImpl.getAllMovieTitles());
        System.out.println();

        System.out.println("movieImpl.getMovieByTitle(\"Spider-Man\")");
        System.out.println(movieImpl.getMovieByTitle("Spider-Man"));
        System.out.println();

        System.out.println("movieImpl.getAllMoviesDetails()");
        System.out.println(movieImpl.getAllMoviesDetails());
        System.out.println();

        // new method testing to get all movies with active status
        System.out.println("movieImpl.getAllActiveMoviesDetails()");
        System.out.println(movieImpl.getAllActiveMoviesDetails());
        System.out.println();

        //List<String> getAllActiveMoviesTitle();
        System.out.println("movieImpl.getAllActiveMoviesTitle()");
        System.out.println(movieImpl.getAllActiveMoviesTitle());
        System.out.println();

        // return a list of movie title and image url with true active status
        //List<String> getAllActiveMoviesTitleAndImageUrl();
        System.out.println("movieImpl.getAllActiveMoviesTitleAndImageUrl()");
        System.out.println(movieImpl.getAllActiveMoviesTitleAndImageUrl());
        System.out.println();

    }

    @Test
    void createMethod()
    {
        // test create movie method
        System.out.println("movieImpl.createMovie(\"Pokemon Diamond and Pearl\", " +
                "\"Adventurous\", \"A teenager dreams to become the Pokemon Master\", LocalDate.of(1997, 5, 3), " +
                "\"pokemon image URL\", \"pokemon landscape URL\", true, \"pg13\")");
        movieImpl.createMovie(
                "Pokemon Diamond and Pearl", "Adventurous",
                "A teenager dreams to become the Pokemon Master",
                LocalDate.of(1997, 5, 3),
                "pokemon image URL",
                "pokemon landscape URL",
                "pg13");
        System.out.println();

    }

    @Test
    void updateMethod()
    {
        movieImpl.updateMovieByIsActive("Inception", false);
        // display all details for all movies
        for (String title : movieImpl.getAllMovieTitles())
            System.out.println(movieImpl.getMovieByTitle(title));
        System.out.println("Done for update movie status method");
    }

    //@Transactional
    @Test
    void deleteMethod()
    {
        // Test delete movie method
        try {
            System.out.println("movieImpl.deleteMovieByTitle(\"Black Adam\")");
            movieImpl.deleteMovieByTitle("Black Adam");
            System.out.println();
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException thrown");
            Assertions.assertEquals("The movie cannot be deleted because it has screenings.", e.getMessage());
        }

    }

    @Test
    void controllerMethod()
    {
        // test getActiveMovieByTitle method
        System.out.println("movieController.getActiveMovieByTitle(\"Avatar\")");
        System.out.println(movieImpl.getActiveMovieByTitle("Avatar"));
        System.out.println();


    }

    @Test
    void violationMethod()
    {
        System.out.println("movieImpl.deleteMovieByTitle(\"Spider-Man\")");
        movieImpl.deleteMovieByTitle("Spider-Man");
        System.out.println();

    }



}
