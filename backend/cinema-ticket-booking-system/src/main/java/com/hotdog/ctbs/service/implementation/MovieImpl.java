package com.hotdog.ctbs.service.implementation;
/*
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

 */
import com.hotdog.ctbs.entity.Movie;
import com.hotdog.ctbs.repository.MovieRepository;
import org.springframework.stereotype.Service;
import com.hotdog.ctbs.service.MovieService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class MovieImpl implements MovieService{

    private final MovieRepository movieRepository;

    private enum ContentRating {
        g, pg, pg13, nc16, m18, r21
    }


    public MovieImpl(MovieRepository movieRepository)
    {
        this.movieRepository = movieRepository;
    }

    @Override
    public List<String> getAllTitles()
    {
        return movieRepository.findAll().stream()
                                    .map(Movie::getTitle)
                                    .toList();
    }

    //return a list of image URL
    @Override
    public List<String> getAllImages()
    {
        return movieRepository.findAll().stream()
                                    .map(Movie::getImageUrl)
                                    .toList();
    }

    // get the movie by input its title
    @Override
    public Movie getMovieByTitle(final String title)
    {
        return movieRepository.findMovieByTitle(title);
    }

    // get the movie by input its id
    @Override
    public Movie getMovieById(final UUID id)
    {
        return movieRepository.findMovieById(id);
    }

    // get the id by input its title
    @Override
    public UUID getIdByTitle(final String title)
    {
        for (Movie movie : movieRepository.findAll())
            if (movie.getTitle().equals(title))
                return movie.getId();
        return null;
    }

    // get a list of content rating for all existing movie in the database
    @Override
    public List<String> getAllContentRating()
    {
        return movieRepository.findAll().stream()
                                    .map(Movie::getContentRating)
                                    .toList();
    }

    // get a list of validated of content rating
    @Override
    public List<String> getValidContentRating()
    {
        return List.of(ContentRating.values()).stream()
                                                .map(Enum::name)
                                                .toList();
    }

    // create a new movie with the given information
    @Override
    public void createMovie (String title, String genre, String description,
                             LocalDate releaseDate, String imageUrl, String contentRating)
    {

        for (String existingMovieTitle : getAllTitles())
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
                        .genre(genre)
                        .description(description)
                        .releaseDate(releaseDate)
                        .imageUrl(imageUrl)
                        .contentRating(contentRating)
                        .build()
        );
    }

    @Override
    public void updateOneMovieByTitle(String targetTitle, String newTitle)
    {
        // the new movie Title must not be the same as any existing title in database
        for (String existingMovieTitle : getAllTitles())
            if (existingMovieTitle.equalsIgnoreCase(newTitle))
                throw new IllegalArgumentException("The movie title already exists.");

        boolean movieFound = false;
        for (Movie movie : movieRepository.findAll()) {
            if (movie.getTitle().equals(targetTitle)){
                movie.setTitle(newTitle);
                movieRepository.save(movie);
                movieFound = true;
                break;
            }

        }
        if (!movieFound){
            throw new IllegalArgumentException("The movie title you would " +
                    "like to update does not exist.");
        }
    }

    /*
    // need to update the movie description
    @Override
    public void updateOneMovieByDescription(String targetTitle, String newDescription)
    {
        for (Movie movie : movieRepository.findAll()) {
            if (movie.getTitle().equals(targetTitle)){
                movie.setDescription(newDescription);
                movieRepository.save(movie);
                break;
            }

        }
    }

    // need to update the movie genre
    @Override
    public void updateOneMovieByGenre(String targetTitle, String newGenre)
    {
        for (Movie movie : movieRepository.findAll()) {
            if (movie.getTitle().equals(targetTitle)){
                movie.setGenre(newGenre);
                movieRepository.save(movie);
                break;
            }

        }
    }

    // delete the movie by input its title *** whether should suspend
    // due to it might be used in other table (screening)
    @Override
    public void deleteMovieByTitle(String title)
    {
        for (Movie movie : movieRepository.findAll()) {
            if (movie.getTitle().equals(title)){
                movieRepository.delete(movie);
                System.out.println("Movie " + title + " has been deleted.");
                break;
            }

        }
    }

    @Override
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
    */



}
