package com.hotdog.ctbs.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.hotdog.ctbs.entity.Movie;
import com.hotdog.ctbs.repository.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.hotdog.ctbs.service.MovieService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class MovieImpl implements MovieService{

    private final MovieRepository movieRepository;

    //this is the enum for content rating (follow the movie "constraints"
    private enum ContentRating {
        g, pg, pg13, nc16, m18, r21
    }


    public MovieImpl(MovieRepository movieRepository)
    {
        this.movieRepository = movieRepository;
    }

        // return a list of titles of all movies
    @Override
    public List<String> getAllMovieTitles()
    {
        return movieRepository.findAll().stream()
                                    .map(Movie::getTitle)
                                    .toList();
    }

    // return a list of genres of all movies
    @Override
    public List<String> getAllMovieGenres()
    {
        return movieRepository.findAll().stream()
                                    .map(Movie::getGenre)
                                    .toList();
    }

    // return a list of descriptions of all movies
    @Override
    public List<String> getAllMovieDescriptions()
    {
        return movieRepository.findAll().stream()
                                    .map(Movie::getDescription)
                                    .toList();
    }

    // return a list of release dates of all movies
    @Override
    public List<LocalDate> getAllMovieReleaseDates()
    {
        return movieRepository.findAll().stream()
                                    .map(Movie::getReleaseDate)
                                    .toList();
    }

    //return a list of image URL of all movies
    @Override
    public List<String> getAllMovieImagesURL()
    {
        return movieRepository.findAll().stream()
                                    .map(Movie::getImageUrl)
                                    .toList();
    }

    // return a list of content rating for all existing movies in the database (might not be used)
    @Override
    public List<String> getAllContentRating()
    {
        return movieRepository.findAll().stream()
                .map(Movie::getContentRating)
                .toList();
    }

    // return a list of valid content ratings (followed the check constraint)
    @Override
    public List<String> getValidContentRating()
    {
        return Stream.of(ContentRating.values())
                .map(Enum::name)
                .toList();
    }

    // return the movie by input its title
    @Override
    public Movie getMovieByTitle(final String title)
    {
        return movieRepository.findMovieByTitle(title);
    }

    // return the movie by input its id
    @Override
    public Movie getMovieById(final UUID id)
    {
        // throw illegal argument if movie is not found
        if (movieRepository.findMovieById(id) == null)
            throw new IllegalArgumentException("The movie id does not exist.");

        return movieRepository.findMovieById(id);
    }

    // return the movie's id by input its title
    @Override
    public UUID getMovieIdByTitle(final String title)
    {
        // throw illegal argument if movie is not found
        for (Movie movie : movieRepository.findAll())
            if (movie.getTitle().equals(title))
                return movie.getId();

        throw new IllegalArgumentException("The movie title does not exist.");
    }

    // return a list of all movies details
    public List<Movie> getAllMoviesDetails() {
        return movieRepository.findAll();
    }

    // create a new movie
    @Override
    public void createMovie (String title, String genre, String description,
                             LocalDate releaseDate, String imageUrl, String landscapeImageUrl, boolean isActive, String contentRating)
    {
        // the new movie Title must not be the same as any existing title in database (1st check)
        // the content rating must be in lowercase form (2nd check)
        // the content rating must be one of the valid content rating (3trd check)
        for (String existingMovieTitle : getAllMovieTitles())
            if (existingMovieTitle.equalsIgnoreCase(title))
                throw new IllegalArgumentException("The movie title already exists.");
        if (!contentRating.equals(contentRating.toLowerCase())) {
            throw new IllegalArgumentException("The content rating given must be in lowercase form.");}
        if(!getValidContentRating().contains(contentRating.toLowerCase()))
            throw new IllegalArgumentException("The content rating given is invalid.");

        movieRepository.save(
                Movie.builder()
                        .id(UUID.randomUUID())
                        .title(title)
                        .genre(genre.toLowerCase())
                        .description(description)
                        .releaseDate(releaseDate)
                        .imageUrl(imageUrl)
                        .landscapeImageUrl(landscapeImageUrl)
                        .isActive(isActive)
                        .contentRating(contentRating)
                        .build()
        );
    }

    // all the methods to update movie with different attributes
    // update the movie's title by input its title and new title
    @Override
    public void updateMovieByTitle(String targetTitle, String newTitle)
    {
        boolean movieFound = false;

        // the new movie Title must not be the same as any existing title in database
        for (String existingMovieTitle : getAllMovieTitles())
            if (existingMovieTitle.equalsIgnoreCase(newTitle))
                throw new IllegalArgumentException("The movie title already exists.");

        // update the movie title if found the existing movie title
        for (Movie exsitingMovie : movieRepository.findAll()) {
            if (exsitingMovie.getTitle().equals(targetTitle)){
                exsitingMovie.setTitle(newTitle);
                movieRepository.save(exsitingMovie);
                movieFound = true;
                break;
            }

        }
        // if the movie title that would to be updated is not found, throw an exception
        if (!movieFound){
            throw new IllegalArgumentException("The movie title you would " +
                    "like to update does not exist.");
        }
    }

    // update the movie's genre by input its title and new genre
    @Override
    public void updateMovieByGenre(String targetTitle, String newGenre)
    {
        boolean movieFound = false;
        // update the movie genre if found the existing movie title
        for (Movie exsitingMovie : movieRepository.findAll()) {
            if (exsitingMovie.getTitle().equals(targetTitle)){
                exsitingMovie.setGenre(newGenre.toLowerCase());
                movieRepository.save(exsitingMovie);
                movieFound = true;
                break;
            }

        }
        // if the movie title that would to be updated is not found, throw an exception
        if (!movieFound){
            throw new IllegalArgumentException("The movie title you would " +
                    "like to update does not exist.");
        }
    }


    // update the movie's description by input its title and new description
    @Override
    public void updateMovieByDescription(String targetTitle, String newDescription)
    {
        boolean movieFound = false;
        // update the movie description if found the existing movie title
        for (Movie exsitingMovie : movieRepository.findAll()) {
            if (exsitingMovie.getTitle().equals(targetTitle)){
                exsitingMovie.setDescription(newDescription);
                movieRepository.save(exsitingMovie);
                movieFound = true;
                break;
            }

        }
        // if the movie title that would to be updated is not found, throw an exception
        if (!movieFound){
            throw new IllegalArgumentException("The movie title you would " +
                    "like to update does not exist.");
        }
    }

    // update the movie's release date by input its title and new release date
    @Override
    public void updateMovieByReleaseDate(String targetTitle, LocalDate newReleaseDate)
    {
        boolean movieFound = false;
        // update the movie release date if found the existing movie title
        for (Movie exsitingMovie : movieRepository.findAll()) {
            if (exsitingMovie.getTitle().equals(targetTitle)){
                exsitingMovie.setReleaseDate(newReleaseDate);
                movieRepository.save(exsitingMovie);
                movieFound = true;
                break;
            }

        }
        // if the movie title that would to be updated is not found, throw an exception
        if (!movieFound){
            throw new IllegalArgumentException("The movie title you would " +
                    "like to update does not exist.");
        }
    }

    // update the movie's image url by input its title and new image url
    @Override
    public void updateMovieByImageUrl(String targetTitle, String newImageUrl)
    {
        boolean movieFound = false;
        // update the movie's image URL if found the existing movie title
        for (Movie exsitingMovie : movieRepository.findAll()) {
            if (exsitingMovie.getTitle().equals(targetTitle)){
                exsitingMovie.setImageUrl(newImageUrl);
                movieRepository.save(exsitingMovie);
                movieFound = true;
                break;
            }

        }
        // if the movie title that would to be updated is not found, throw an exception
        if (!movieFound){
            throw new IllegalArgumentException("The movie title you would " +
                    "like to update does not exist.");
        }
    }

    // update movie by landscape image url
    @Override
    public void updateMovieByLandscapeImageUrl(String targetTitle, String newLandscapeImageUrl)
    {
        boolean movieFound = false;
        // update the movie's landscape image URL if found the existing movie title
        for (Movie exsitingMovie : movieRepository.findAll()) {
            if (exsitingMovie.getTitle().equals(targetTitle)){
                exsitingMovie.setLandscapeImageUrl(newLandscapeImageUrl);
                movieRepository.save(exsitingMovie);
                movieFound = true;
                break;
            }

        }
        // if the movie title that would to be updated is not found, throw an exception
        if (!movieFound){
            throw new IllegalArgumentException("The movie title you would " +
                    "like to update does not exist.");
        }
    }

    // update the movie's active status by input its title and new active status
    @Override
    public void updateMovieByIsActive(String targetTitle, boolean newIsActive)
    {
        boolean movieFound = false;
        // update the movie status if found the existing movie title
        for (Movie exsitingMovie : movieRepository.findAll()) {
            if (exsitingMovie.getTitle().equals(targetTitle)){
                exsitingMovie.setActive(newIsActive);
                movieRepository.save(exsitingMovie);
                movieFound = true;
                break;
            }

        }
        // if the movie title that would to be updated is not found, throw an exception
        if (!movieFound){
            throw new IllegalArgumentException("The movie title you would " +
                    "like to update does not exist.");
        }
    }

    // update the movie's content rating by input its title and new content rating
    @Override
    public void updateMovieByContentRating(String targetTitle, String newContentRating)
    {
        boolean movieFound = false;

        // make sure the new content rating follow the format and is valid
        if (!newContentRating.equals(newContentRating.toLowerCase())) {
            throw new IllegalArgumentException("The content rating given must be in lowercase form.");}
        if(!getValidContentRating().contains(newContentRating.toLowerCase()))
            throw new IllegalArgumentException("The content rating given is invalid.");

        // update the content rating if found the existing movie title
        for (Movie exsitingMovie : movieRepository.findAll()) {
            if (exsitingMovie.getTitle().equals(targetTitle)){
                exsitingMovie.setContentRating(newContentRating);
                movieRepository.save(exsitingMovie);
                movieFound = true;
                break;
            }

        }
        // if the movie title that would to be updated is not found, throw an exception
        if (!movieFound){
            throw new IllegalArgumentException("The movie title you would " +
                    "like to update does not exist.");
        }
    }

    // update a movie with entering all attributes
    @Override
    public void updateMovieByAllAttributes(String targetTitle, String newTitle, String newGenre, String newDescription,
                                           LocalDate newReleaseDate, String newImageUrl, String newLandscapeImageUrl, boolean newIsActive, String newContentRating) {
        boolean movieFound = false;
        // update everything if found the existing movie title
        for (Movie exsitingMovie : movieRepository.findAll()) {
            if (exsitingMovie.getTitle().equals(targetTitle)) {
                exsitingMovie.setTitle(newTitle);
                exsitingMovie.setGenre(newGenre.toLowerCase());
                exsitingMovie.setDescription(newDescription);
                exsitingMovie.setReleaseDate(newReleaseDate);
                exsitingMovie.setImageUrl(newImageUrl);
                exsitingMovie.setLandscapeImageUrl(newLandscapeImageUrl);
                exsitingMovie.setActive(newIsActive);
                exsitingMovie.setContentRating(newContentRating);
                movieRepository.save(exsitingMovie);
                movieFound = true;
                break;
            }

        }
        // if the movie title that would to be updated is not found, throw an exception
        if (!movieFound) {
            throw new IllegalArgumentException("The movie title you would " +
                    "like to update does not exist.");
        }

    }


    // delete the movie by input its title ( still in progress)
    // consideration : if delete movie ==  delete all the screening related to this deleted movies
    // or isAvailable to determine whether the movie can be deleted
    @Transactional
    @Override
    public void deleteMovieByTitle(String title)
    {
        for (Movie existingMovie : movieRepository.findAll()) {
            if (existingMovie.getTitle().equals(title)){


                if(existingMovie.getScreenings().size() > 0)
                    throw new IllegalArgumentException("The movie cannot be deleted because it has screenings.");
                else{
                    System.out.println("Delete method called");
                    movieRepository.delete(existingMovie);
                    System.out.println("Movie " + title + " has been deleted.");
                    break;
                }

            }

        }
    }

    public String MovieResponse(Movie movie) throws JsonProcessingException
    {
        ObjectMapper om = new ObjectMapper();
        JsonNode jn = om.readTree(om.writeValueAsString(movie));
        ((ObjectNode) jn).remove("id");
        return jn.toString();
    }

    @Override
    public String MoviesResponse(List<Movie> movies) throws JsonProcessingException
    {
        StringBuilder sb = new StringBuilder("[");
        for (Movie movie : movies)
            sb.append(MovieResponse(movie))
                    .append(",");

        return sb.deleteCharAt(sb.length() - 1)
                .append("]")
                .toString();
    }

    @Override
    public Movie MovieRequest(String json) throws JsonProcessingException
    {
        return movieRepository.findMovieByTitle(
                new ObjectMapper().readValue(json, Movie.class).getTitle()
        );
    }

    @Override
    public List<Movie> MoviesRequest(String json) throws JsonProcessingException
    {
        List<Movie> movies =
                new ObjectMapper().readValue(json, new TypeReference<>() {
                });
        return movies.stream()
                .map(movie -> movieRepository.findMovieByTitle(movie.getTitle()))
                .toList();
    }

    // All methods for "active" movies
    @Override
    public List<Movie> getAllActiveMoviesDetails()
    {
        return movieRepository.findAll().stream()
                .filter(Movie::isActive)
                .toList();
    }

    // return a list of movie title with true active status
    @Override
    public List<String> getAllActiveMoviesTitle()
    {
        return movieRepository.findAll().stream()
                .filter(Movie::isActive)
                .map(Movie::getTitle)
                .toList();
    }

    // return a list of movie title and image url with true active status
    @Override
    public List<String> getAllActiveMoviesTitleAndImageUrl()
    {
        return movieRepository.findAll().stream()
                .filter(Movie::isActive)
                .map(movie -> movie.getTitle() + " " + movie.getImageUrl())
                .toList();
    }





}
