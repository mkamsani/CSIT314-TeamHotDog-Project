package com.hotdog.ctbs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotdog.ctbs.entity.Movie;
import com.hotdog.ctbs.repository.MovieRepository;
import jakarta.servlet.http.HttpServletRequest;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import com.hotdog.ctbs.service.implementation.MovieImpl;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/movie")
public class MovieController {

    private final MovieImpl movieImpl;

    public MovieController(MovieImpl movieImpl)
    {
        this.movieImpl = movieImpl;
    }

    // To return a list of all movies title (including active and inactive movies)
    @GetMapping("/read/allMovieTitles")
    public String ReadAllMovieTitles()
    {
        return movieImpl.getAllMovieTitles().toString();
    }

    // To return a list of all movies objects (including active and inactive movies)
    @GetMapping("/read/allMoviesDetails")
    public String ReadAllMoviesDetails()
    {
        return movieImpl.getAllMoviesDetails().toString();
    }

    // To return a list of all "active" movies title
    @GetMapping("/read/allActiveMovieTitles")
    public String ReadAllActiveMovieTitles()
    {
        return movieImpl.getAllActiveMoviesTitle().toString();
    }

    // To return a list of all "active" movies details(object)
    @GetMapping("/read/allActiveMoviesDetails")
    public String ReadAllActiveMoviesDetails()
    {
        return movieImpl.getAllActiveMoviesDetails().toString();
    }

    // create a new movie
    @PostMapping("/create/movie")
    public String CreateMovie(@RequestBody String json)
    {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String title = jsonNode.get("title").asText();
            String genre = jsonNode.get("genre").asText();
            String description = jsonNode.get("description").asText();
            LocalDate releaseDate = LocalDate.parse(jsonNode.get("releaseDate").asText());
            String imageUrl = jsonNode.get("imageUrl").asText();
            boolean isActive = jsonNode.get("isActive").asBoolean();
            String contentRating = jsonNode.get("contentRating").asText();
            movieImpl.createMovie(title, genre, description, releaseDate, imageUrl, isActive, contentRating);
            return "Movie was created successfully.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // Update a movie with all its details (assume one "Submit" button)
    @PutMapping("/update/movie")
    public String UpdateMovie(@RequestBody String json)
    {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String targetTitle = jsonNode.get("targetTitle").asText();
            String newTitle = jsonNode.get("title").asText();
            String newGenre = jsonNode.get("genre").asText();
            String newDescription = jsonNode.get("description").asText();
            LocalDate newReleaseDate = LocalDate.parse(jsonNode.get("releaseDate").asText());
            String newImageUrl = jsonNode.get("imageUrl").asText();
            boolean newIsActive = jsonNode.get("isActive").asBoolean();
            String newContentRating = jsonNode.get("contentRating").asText();
            movieImpl.updateMovieByAllAttributes(targetTitle, newTitle, newGenre, newDescription, newReleaseDate, newImageUrl, newIsActive, newContentRating);
            // if update successfully, print the message
            return "The movie was updated successfully.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // Update movie title
    @PostMapping("/update/movie/Title")
    public String UpdateMovieByTargetTitle(@RequestBody String json)
    {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String targetTitle = jsonNode.get("targetTitle").asText();
            String newTitle = jsonNode.get("title").asText();
            movieImpl.updateMovieByTitle(targetTitle, newTitle);
            // if update successfully, print the message
            return "The movie title was updated successfully.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // Update movie genre
    @PostMapping("/update/movie/Genre")
    public String UpdateMovieByGenre(@RequestBody String json)
    {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String targetTitle = jsonNode.get("targetTitle").asText();
            String newGenre = jsonNode.get("genre").asText();
            movieImpl.updateMovieByGenre(targetTitle, newGenre);
            // if update successfully, print the message
            return "The movie genre was updated successfully.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // Update movie description
    @PostMapping("/update/movie/Description")
    public String UpdateMovieByDescription(@RequestBody String json)
    {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String targetTitle = jsonNode.get("targetTitle").asText();
            String newDescription = jsonNode.get("description").asText();
            movieImpl.updateMovieByDescription(targetTitle, newDescription);
            // if update successfully, print the message
            return "The movie description was updated successfully.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // Update movie release date
    @PostMapping("/update/movie/ReleaseDate")
    public String UpdateMovieByReleaseDate(@RequestBody String json)
    {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String targetTitle = jsonNode.get("targetTitle").asText();
            LocalDate newReleaseDate = LocalDate.parse(jsonNode.get("releaseDate").asText());
            movieImpl.updateMovieByReleaseDate(targetTitle, newReleaseDate);
            // if update successfully, print the message
            return "The movie release date was updated successfully.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // Update movie image url
    @PostMapping("/update/movie/ImageUrl")
    public String UpdateMovieByImageUrl(@RequestBody String json)
    {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String targetTitle = jsonNode.get("targetTitle").asText();
            String newImageUrl = jsonNode.get("imageUrl").asText();
            movieImpl.updateMovieByImageUrl(targetTitle, newImageUrl);
            // if update successfully, print the message
            return "The movie image url was updated successfully.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // Update movie isActive
    @PostMapping("/update/movie/IsActive")
    public String UpdateMovieByIsActive(@RequestBody String json)
    {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String targetTitle = jsonNode.get("targetTitle").asText();
            boolean newIsActive = jsonNode.get("isActive").asBoolean();
            movieImpl.updateMovieByIsActive(targetTitle, newIsActive);
            // if update successfully, print the message
            return "The movie is active status was updated successfully.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // Update movie content rating
    @PostMapping("/update/movie/ContentRating")
    public String UpdateMovieByContentRating(@RequestBody String json)
    {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String targetTitle = jsonNode.get("targetTitle").asText();
            String newContentRating = jsonNode.get("contentRating").asText();
            movieImpl.updateMovieByContentRating(targetTitle, newContentRating);
            // if update successfully, print the message else print error message
            return "The movie content rating was updated successfully.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // Delete a movie by title (still in progress)
    @DeleteMapping("/delete/movie")
    public String DeleteMovie(@RequestBody String json) {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String title = jsonNode.get("title").asText();
            movieImpl.deleteMovieByTitle(title);
            // if delete successfully, print the message
            return "The movie was deleted successfully.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }

    }


}
