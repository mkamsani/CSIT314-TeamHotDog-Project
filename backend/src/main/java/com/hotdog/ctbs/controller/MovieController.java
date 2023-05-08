package com.hotdog.ctbs.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotdog.ctbs.service.implementation.MovieImpl;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/movie")
public class MovieController {

    private final MovieImpl movieImpl;

    public MovieController(MovieImpl movieImpl)
    {
        this.movieImpl = movieImpl;
    }

    // To return a list of all movies title (including active and inactive movies) for manager to select
    @GetMapping("/read/allMovieTitles")
    public String ReadAllMovieTitles()
    {
        System.out.println("MovieController.ReadAllMovieTitles() called.");

        return movieImpl.getAllMovieTitles().toString();
    }

    // To return a list of all movies objects (including active and inactive movies) for manager to select
    @GetMapping("/read/allMoviesDetails")
    public String ReadAllMoviesDetails()
    {
        System.out.println("MovieController.ReadAllMoviesDetails() called.");

        return movieImpl.getAllMoviesDetails().toString();
    }

    // To return a list of all "active" movies title for customer to view
    @GetMapping("/read/allActiveMovieTitles")
    public String ReadAllActiveMovieTitles()
    {
        System.out.println("MovieController.ReadAllActiveMovieTitles() called.");

        return movieImpl.getAllActiveMoviesTitle().toString();
    }

    // To return a list of all "active" movies details(object) for customer to view
    @GetMapping("/read/allActiveMoviesDetails")
    public String ReadAllActiveMoviesDetails()
    {
        System.out.println("MovieController.ReadAllActiveMoviesDetails() called.");
        return movieImpl.getAllActiveMoviesDetails().toString();
    }

    // create a new movie
    @PostMapping("/create/movie")
    public String CreateMovie(@RequestBody String json)
    {
        System.out.println("MovieController.CreateMovie() called.");

        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String title = jsonNode.get("title").asText();
            String genre = jsonNode.get("genre").asText();
            String description = jsonNode.get("description").asText();
            LocalDate releaseDate = LocalDate.parse(jsonNode.get("releaseDate").asText());
            String imageUrl = jsonNode.get("imageUrl").asText();
            String landscapeImageUrl = jsonNode.get("landscapeImageUrl").asText();
            boolean isActive = jsonNode.get("isActive").asBoolean();
            String contentRating = jsonNode.get("contentRating").asText();
            movieImpl.createMovie(title, genre, description, releaseDate, imageUrl, landscapeImageUrl, isActive, contentRating);
            return "Movie was created successfully.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // Update a movie with all its details (assume one "Submit" button)
    @PutMapping("/update/movie")
    public String UpdateMovie(@RequestBody String json)
    {
        System.out.println("MovieController.UpdateMovie() called.");

        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String targetTitle = jsonNode.get("targetTitle").asText();
            String newTitle = jsonNode.get("title").asText();
            String newGenre = jsonNode.get("genre").asText();
            String newDescription = jsonNode.get("description").asText();
            LocalDate newReleaseDate = LocalDate.parse(jsonNode.get("releaseDate").asText());
            String newImageUrl = jsonNode.get("imageUrl").asText();
            String newLandscapeImageUrl = jsonNode.get("landscapeImageUrl").asText();
            boolean newIsActive = jsonNode.get("isActive").asBoolean();
            String newContentRating = jsonNode.get("contentRating").asText();
            movieImpl.updateMovieByAllAttributes(targetTitle, newTitle, newGenre, newDescription, newReleaseDate, newImageUrl, newLandscapeImageUrl, newIsActive, newContentRating);
            // if update successfully, print the message
            return "The movie was updated successfully.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // Update movie title
    @PutMapping("/update/movie/Title")
    public String UpdateMovieByTargetTitle(@RequestBody String json)
    {
        System.out.println("MovieController.UpdateMovieByTargetTitle() called.");

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
    @PutMapping("/update/movie/Genre")
    public String UpdateMovieByGenre(@RequestBody String json)
    {
        System.out.println("MovieController.UpdateMovieByGenre() called.");

        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            System.out.println(jsonNode);
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
    @PutMapping("/update/movie/Description")
    public String UpdateMovieByDescription(@RequestBody String json)
    {
        System.out.println("MovieController.UpdateMovieByDescription() called.");

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
    @PutMapping("/update/movie/ReleaseDate")
    public String UpdateMovieByReleaseDate(@RequestBody String json)
    {
        System.out.println("MovieController.UpdateMovieByReleaseDate() called.");

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
    @PutMapping("/update/movie/ImageUrl")
    public String UpdateMovieByImageUrl(@RequestBody String json)
    {
        System.out.println("MovieController.UpdateMovieByImageUrl() called.");

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

    // Update movie landscape image url
    @PutMapping("/update/movie/LandscapeImageUrl")
    public String UpdateMovieByLandscapeImageUrl(@RequestBody String json)
    {
        System.out.println("MovieController.UpdateMovieByLandscapeImageUrl() called.");

        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String targetTitle = jsonNode.get("targetTitle").asText();
            String newLandscapeImageUrl = jsonNode.get("landscapeImageUrl").asText();
            movieImpl.updateMovieByLandscapeImageUrl(targetTitle, newLandscapeImageUrl);
            // if update successfully, print the message
            return "The movie landscape image url was updated successfully.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // Update movie isActive
    @PutMapping("/update/movie/IsActive")
    public String UpdateMovieByIsActive(@RequestBody String json)
    {
        System.out.println("MovieController.UpdateMovieByIsActive() called.");

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
    @PutMapping("/update/movie/ContentRating")
    public String UpdateMovieByContentRating(@RequestBody String json)
    {
        System.out.println("MovieController.UpdateMovieByContentRating() called.");

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

    // Delete a movie by title (** constraint: if any movie linked to screenings, cannot delete)
    @DeleteMapping("/delete/movie")
    public String DeleteMovie(@RequestBody String json) {
        System.out.println("MovieController.DeleteMovie() called.");

        try {
            System.out.println(json);
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
