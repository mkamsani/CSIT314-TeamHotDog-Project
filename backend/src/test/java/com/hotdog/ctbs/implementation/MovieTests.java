package com.hotdog.ctbs.implementation;

import com.hotdog.ctbs.entity.Movie;
import com.hotdog.ctbs.service.implementation.MovieImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.UUID;

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

        System.out.println("movieImpl.getAllMovieGenres()");
        System.out.println(movieImpl.getAllMovieGenres());
        System.out.println();

        System.out.println("movieImpl.getAllMovieDescriptions()");
        System.out.println(movieImpl.getAllMovieDescriptions());
        System.out.println();

        System.out.println("movieImpl.getAllMovieReleaseDates()");
        System.out.println(movieImpl.getAllMovieReleaseDates());
        System.out.println();

        System.out.println("movieImpl.getAllMovieImagesURL()");
        System.out.println(movieImpl.getAllMovieImagesURL());
        System.out.println();

        System.out.println("movieImpl.getAllContentRating()");
        System.out.println(movieImpl.getAllContentRating());
        System.out.println();

        System.out.println("movieImpl.getValidContentRating()");
        System.out.println(movieImpl.getValidContentRating());
        System.out.println();

        System.out.println("movieImpl.getMovieByTitle(\"Spider-Man\")");
        System.out.println(movieImpl.getMovieByTitle("Spider-Man"));
        System.out.println();

        /*System.out.println("movieImpl.getMovieById(UUID.fromString(\"690464fc-5beb-4ed8-90b3-cd2454b53496\"))");
        System.out.println(movieImpl.getMovieById(UUID.fromString("690464fc-5beb-4ed8-90b3-cd2454b53496")));
        System.out.println();*/

        System.out.println("movieImpl.getMovieIdByTitle(\"Spider-Man\")");
        System.out.println(movieImpl.getMovieIdByTitle("Spider-Man"));
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
        // To check the list of movies if the movie is created
        System.out.println(movieImpl.getAllMovieTitles());
    }

    @Test
    void updateMethod()
    {
        //
        // test update movie method (using title)
        System.out.println("The list of existing movie titles before an update made");
        System.out.println(movieImpl.getAllMovieTitles());
        System.out.println();

        System.out.println("movieImpl.updateOneMovieByTitle(\"Wonder Woman\", \"PowerPuffGirl\")");
        movieImpl.updateMovieByTitle("Wonder Woman", "PowerPuffGirl");
        System.out.println();

        System.out.println("The list of existing movie titles after an update made");
        System.out.println(movieImpl.getAllMovieTitles());
        System.out.println("Done for update movie title method");

        //
        // test update movie method (using genre)
        System.out.println("The list of existing movie genres before an update made");
        System.out.println(movieImpl.getAllMovieGenres());
        System.out.println();

        movieImpl.updateMovieByGenre("Spider-Man", "new SpiderMan genre");
        System.out.println("The list of existing movie genres after an update made");
        // display all details for all movies
        for (String title : movieImpl.getAllMovieTitles())
            System.out.println(movieImpl.getMovieByTitle(title));
        System.out.println("Done for update movie genre method");

        //
        // test update movie method (using description)
        System.out.println("The list of existing movie description before an update made");
        for (String title : movieImpl.getAllMovieTitles())
            System.out.println(movieImpl.getMovieByTitle(title));

        movieImpl.updateMovieByDescription("Inception", "new Inception description");
        // display all details for all movies
        for (String title : movieImpl.getAllMovieTitles())
            System.out.println(movieImpl.getMovieByTitle(title));
        System.out.println("Done for update movie description method");

        //
        // test update movie method (using release date)
        System.out.println("The list of existing movie release date before an update made");
        for (String title : movieImpl.getAllMovieTitles())
            System.out.println(movieImpl.getMovieByTitle(title));

        movieImpl.updateMovieByReleaseDate("Inception", LocalDate.of(1997, 5, 25));
        // display all details for all movies
        for (String title : movieImpl.getAllMovieTitles())
            System.out.println(movieImpl.getMovieByTitle(title));
        System.out.println("Done for update movie release date method");

        //
        // test update movie method (using image url)
        System.out.println("The list of existing movie image url before an update made");
        for (String title : movieImpl.getAllMovieTitles())
            System.out.println(movieImpl.getMovieByTitle(title));

        movieImpl.updateMovieByImageUrl("Matrix", "Matrix newUpdateImageURL");
        // display all details for all movies
        for (String title : movieImpl.getAllMovieTitles())
            System.out.println(movieImpl.getMovieByTitle(title));
        System.out.println("Done for update movie image url method");

        //
        // test update movie method (using landscape image url)
        System.out.println("The list of existing movie landscape image url before an update made");
        for (String title : movieImpl.getAllMovieTitles())
            System.out.println(movieImpl.getMovieByTitle(title));

        movieImpl.updateMovieByLandscapeImageUrl("Avatar","Avatar newUpdateLandscapeURL");
        // display all details for all movies
        for (String title : movieImpl.getAllMovieTitles())
            System.out.println(movieImpl.getMovieByTitle(title));
        System.out.println("Done for update movie landscape image url method");

        //
        // test update movie method (using status)
        System.out.println("The list of existing movie status before an update made");
        for (String title : movieImpl.getAllMovieTitles())
            System.out.println(movieImpl.getMovieByTitle(title));

        movieImpl.updateMovieByIsActive("Inception", false);
        // display all details for all movies
        for (String title : movieImpl.getAllMovieTitles())
            System.out.println(movieImpl.getMovieByTitle(title));
        System.out.println("Done for update movie status method");

        //
        // test update movie method (using content rating)
        System.out.println("The list of existing movie content rating before an update made");
        for (String title : movieImpl.getAllMovieTitles())
            System.out.println(movieImpl.getMovieByTitle(title));

        movieImpl.updateMovieByContentRating("Spider-Man", "pg13");
        // display all details for all movies
        for (String title : movieImpl.getAllMovieTitles())
            System.out.println(movieImpl.getMovieByTitle(title));
        System.out.println("Done for update movie content rating method");

        //
        // test update movie method (using all attributes)
        System.out.println("The list of existing movie before an update made");
        for (String title : movieImpl.getAllMovieTitles())
            System.out.println(movieImpl.getMovieByTitle(title));

        // display all details for all movies
        for (String title : movieImpl.getAllMovieTitles())
            System.out.println(movieImpl.getMovieByTitle(title));
        System.out.println("Done for update movie all attributes method");

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
    void violationMethod(){

    }



}
