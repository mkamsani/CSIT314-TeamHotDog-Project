package com.hotdog.ctbs.implementation;

import com.hotdog.ctbs.service.implementation.MovieImpl;
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

        System.out.println("movieImpl.getMovieById(UUID.fromString(\"690464fc-5beb-4ed8-90b3-cd2454b53496\"))");
        System.out.println(movieImpl.getMovieById(UUID.fromString("690464fc-5beb-4ed8-90b3-cd2454b53496")));
        System.out.println();

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
                        "\"Adventurous\", \"A teenager dreams to become the Pokemon Master\", " +
                        "LocalDate.of(1997, 5, 3), " +
                        "\"https://www.imdb.com/title/tt0145487/mediaviewer/rm4039471360/\", " +
                        "true, " +
                        "\"PG13\")");
        movieImpl.createMovie(
                "Pokemon Diamond and Pearl", "Adventurous",
                "A teenager dreams to become the Pokemon Master",
                LocalDate.of(1997, 5, 3),
                "https://www.imdb.com/title/tt0145487/mediaviewer/rm4039471360/",
                true,
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
        movieImpl.updateMovieByGenre("PowerPuffGirl", "Action");
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

        movieImpl.updateMovieByDescription("Ultraman", "A story about an alien superhero");
        // display all details for all movies
        for (String title : movieImpl.getAllMovieTitles())
            System.out.println(movieImpl.getMovieByTitle(title));
        System.out.println("Done for update movie description method");

        //
        // test update movie method (using release date)
        System.out.println("The list of existing movie release date before an update made");
        for (String title : movieImpl.getAllMovieTitles())
            System.out.println(movieImpl.getMovieByTitle(title));

        movieImpl.updateMovieByReleaseDate("Ultraman", LocalDate.of(1997, 5, 25));
        // display all details for all movies
        for (String title : movieImpl.getAllMovieTitles())
            System.out.println(movieImpl.getMovieByTitle(title));
        System.out.println("Done for update movie release date method");

        //
        // test update movie method (using image url)
        System.out.println("The list of existing movie image url before an update made");
        for (String title : movieImpl.getAllMovieTitles())
            System.out.println(movieImpl.getMovieByTitle(title));

        movieImpl.updateMovieByImageUrl("Ultraman", "https://www.imdb.com/title/tt0145487/mediaviewer/rm4039471360/");
        // display all details for all movies
        for (String title : movieImpl.getAllMovieTitles())
            System.out.println(movieImpl.getMovieByTitle(title));
        System.out.println("Done for update movie image url method");

        //
        // test update movie method (using status)
        System.out.println("The list of existing movie status before an update made");
        for (String title : movieImpl.getAllMovieTitles())
            System.out.println(movieImpl.getMovieByTitle(title));

        movieImpl.updateMovieByIsActive("Ultraman", false);
        // display all details for all movies
        for (String title : movieImpl.getAllMovieTitles())
            System.out.println(movieImpl.getMovieByTitle(title));
        System.out.println("Done for update movie status method");


        //
        // test update movie method (using content rating)
        System.out.println("The list of existing movie content rating before an update made");
        for (String title : movieImpl.getAllMovieTitles())
            System.out.println(movieImpl.getMovieByTitle(title));

        movieImpl.updateMovieByContentRating("Ultraman", "pg13");
        // display all details for all movies
        for (String title : movieImpl.getAllMovieTitles())
            System.out.println(movieImpl.getMovieByTitle(title));
        System.out.println("Done for update movie content rating method");

        //
        // test update movie method (using all attributes)
        System.out.println("The list of existing movie before an update made");
        for (String title : movieImpl.getAllMovieTitles())
            System.out.println(movieImpl.getMovieByTitle(title));

        movieImpl.updateMovieByAllAttributes("Ultraman", "Ultraman2", "Barbie girl", "A story about an alien superhero", LocalDate.of(1997, 5, 25), "https://www.imdb.com/title/tt0145487/mediaviewer/rm4039471360/", false, "pg13");

        // display all details for all movies
        for (String title : movieImpl.getAllMovieTitles())
            System.out.println(movieImpl.getMovieByTitle(title));
        System.out.println("Done for update movie all attributes method");

    }

    @Test
    void deleteMethod()
    {
        // test delete movie method
        System.out.println("The existing movie titles before a deletion made");
        System.out.println(movieImpl.getAllMovieTitles());
        System.out.println("movieImpl.deleteOneMovieByTitle(\"Ultraman\")");
        movieImpl.deleteMovieByTitle("Ultraman");
        System.out.println();
        System.out.println(movieImpl.getAllMovieTitles());
    }


}
