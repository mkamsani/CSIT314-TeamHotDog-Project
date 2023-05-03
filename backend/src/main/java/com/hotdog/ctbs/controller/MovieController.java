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
@RequestMapping("/api/movie")
public class MovieController {

    private final MovieImpl movieImpl;

    public MovieController(MovieImpl movieImpl)
    {
        this.movieImpl = movieImpl;
    }

    // To show a list of all movies title (including active and inactive movies)
    @GetMapping("/read/allMovieTitles")
    public String ReadAllMovieTitles()
    {
        return movieImpl.getAllMovieTitles().toString();
    }

    // To show a list of all movies objects (including active and inactive movies)
    @GetMapping("/read/allMoviesDetails")
    public String ReadAllMoviesDetails()
    {
        return movieImpl.getAllMoviesDetails().toString();
    }

    // To show a list of all active movies title
    @GetMapping("/read/allActiveMovieTitles")
    public String ReadAllActiveMovieTitles()
    {
        return movieImpl.getAllActiveMoviesTitle().toString();
    }

    // To show a list of all active movies details(object)
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
            return "Success creating movie";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // update a movie
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
            return "Success updating movie";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }


}
