package com.hotdog.ctbs.service;

//import com.fasterxml.jackson.core.JsonProcessingException;
import com.hotdog.ctbs.entity.Movie;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
public interface MovieService {

    // return a list of all movie titles
    List<String> getAllTitles();

    //return a list of image URL
    List<String> getAllImages();


    // get the movie by input its title
    Movie getMovieByTitle(final String title);

    // get the movie by input its id
    Movie getMovieById(final UUID id);

    // get the id by input its title
    UUID getIdByTitle(final String title);

    // get a list of content rating for all existing movie in the database
    List<String> getAllContentRating();

    // get a list of validated of content rating
    List<String> getValidContentRating();

    void createMovie (String title, String genre, String description,
                             LocalDate releaseDate, String imageUrl, String contentRating);

    void updateOneMovieByTitle(String targetTitle, String newTitle);

    /*
    // need to update the movie description
    void updateOneMovieByDescription(String targetTitle, String newDescription);

    // update the movie genre
    void updateOneMovieByGenre(String targetTitle, String newGenre);

    // delete the movie by input its title *** whether should suspend
    // due to it might be used in other table (screening)
    void deleteMovieByTitle(String title);

    String MovieResponse(Movie movie) throws JsonProcessingException;

    String MoviesResponse(List<Movie> movies) throws JsonProcessingException;

    Movie MovieRequest(String json) throws JsonProcessingException;

    List<Movie> MoviesRequest(String json) throws JsonProcessingException;

     */


}
