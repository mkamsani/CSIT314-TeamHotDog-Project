package com.hotdog.ctbs.controller.manager;

// Application imports.
import com.hotdog.ctbs.service.implementation.MovieImpl;

// Java imports.
import java.time.LocalDate;

// JSON deserialization imports.
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// Spring imports.
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/manager/movie")
public class MovieCreateController {

    private final MovieImpl movieImpl;
    private final ObjectMapper objectMapper;

    public MovieCreateController(MovieImpl movieImpl)
    {
        this.movieImpl = movieImpl;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    /*
     MovieCreateController
     Method will be used:
     createMovie(String title, String genre, String description, LocalDate releaseDate,
                 String imageUrl, String landscapeImageUrl, boolean isActive,
                 String contentRating)   - creates a new movie.
     */

    // create a new movie
    // Invoke-WebRequest -Method POST -Headers @{"Content-Type"="application/json"} -Body '{"title":"The Haha","genre":"Action","description":"A computer hacker learns from mysterious rebels about the true nature of his controllers.","releaseDate":"1999-03-31","imageUrl":"https://www.themoviedb.org/t/p/w600_and_h900_bestv2/hEpWvX6Bp79eLxY1kX5ZZJcme5U.jpg","landscapeImageUrl":"https://www.themoviedb.org/t/p/w1920/3KN24PrOheHVYs9ypuOIdFBEpX.jpg","isActive":true,"contentRating":"pg13"}' -Uri http://localhost:8000/api/manager/movie/create/movie
    @PostMapping("/create/movie")
    public String CreateMovie(@RequestBody final String json)
    {
        System.out.println("MovieCreateController.CreateMovie() called.");

        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            movieImpl.createMovie(
                    jsonNode.get("title").asText(),
                    jsonNode.get("genre").asText(),
                    jsonNode.get("description").asText(),
                    LocalDate.parse(jsonNode.get("releaseDate").asText()),
                    jsonNode.get("imageUrl").asText(),
                    jsonNode.get("landscapeImageUrl").asText(),
                    jsonNode.get("isActive").asBoolean(),
                    jsonNode.get("contentRating").asText()

            );
            return "Movie was created successfully.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

}
