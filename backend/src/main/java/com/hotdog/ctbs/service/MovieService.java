package com.hotdog.ctbs.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hotdog.ctbs.entity.Movie;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
public interface MovieService {

    // return a list of titles of all movies
    List<String> getAllMovieTitles();

    // return a list of genres of all movies
    List<String> getAllMovieGenres();

    // return a list of descriptions of all movies
    List<String> getAllMovieDescriptions();

    // return a list of release dates of all movies
    List<LocalDate> getAllMovieReleaseDates();

    // return a list of image URLs of all movies
    List<String> getAllMovieImagesURL();

    // return a list of content ratings of all movies
    List<String> getAllContentRating();

    // return a list of valid content ratings
    List<String> getValidContentRating();

    // return the movie by input its title
    Movie getMovieByTitle(final String title);

    // return the movie by input its id
    Movie getMovieById(final UUID id);

    // return the movie id by input its title
    UUID getMovieIdByTitle(final String title);

    // return a list of all movies
    List<Movie> getAllMoviesDetails();

    // create a new movie
    void createMovie (String title, String genre, String description,
                             LocalDate releaseDate, String imageUrl, String landscapeImageUrl,boolean isActive,String contentRating);

    // all the methods to update movie with different attributes
    // update the movie's title by input its title and new title
    void updateMovieByTitle(String targetTitle, String newTitle);

    // update the movie's genre by input its title and new genre
    void updateMovieByGenre(String targetTitle, String newGenre);

    // update the movie's description by input its title and new description
    void updateMovieByDescription(String targetTitle, String newDescription);

    // update the movie's release date by input its title and new release date
    void updateMovieByReleaseDate(String targetTitle, LocalDate newReleaseDate);

    // update the movie's image url by input its title and new image url
    void updateMovieByImageUrl(String targetTitle, String newImageUrl);

    // update the movie's landscape image url by input its title and new landscape image url
    void updateMovieByLandscapeImageUrl(String targetTitle, String newLandscapeImageUrl);

    // update the movie's is active by input its title and new is active
    void updateMovieByIsActive(String targetTitle, boolean newIsActive);

    // update the movie's content rating by input its title and new content rating
    void updateMovieByContentRating(String targetTitle, String newContentRating);

    // delete the movie by input its title
    // consideration : if delete movie ==  delete all the screening related to this deleted movies
    // or isAvailable to determine whether the movie can be deleted
    void deleteMovieByTitle(String title);


    String MoviesResponse(List<Movie> movies) throws JsonProcessingException;


    Movie MovieRequest(String json) throws JsonProcessingException;

    List<Movie> MoviesRequest(String json) throws JsonProcessingException;

    // Active movie methods
    //***new method return a list of movie with true active status
    List<Movie> getAllActiveMoviesDetails();

    List<String> getAllActiveMoviesTitle();

    // return a list of movie title and image url with true active status
    List<String> getAllActiveMoviesTitleAndImageUrl();

    // updateMovieByAllAttributes
    // update the movie's all attributes by input its title and new attributes
    void updateMovieByAllAttributes(String targetTitle, String newTitle, String newGenre, String newDescription,
                                    LocalDate newReleaseDate, String newImageUrl, String newLandscapeImageUrl, boolean newIsActive, String newContentRating);

}
