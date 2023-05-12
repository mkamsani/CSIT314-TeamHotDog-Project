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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/manager/movie")
public class MovieUpdateController{

    private final MovieImpl movieImpl;
    private final ObjectMapper objectMapper;

    public MovieUpdateController(MovieImpl movieImpl)
    {
        this.movieImpl = movieImpl;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    /*
     MovieUpdateController
     Method will be used:
      updateMovie(String targetMovieTitle, String title, String genre,
                   String description, LocalDate releaseDate, String imageUrl,
                   String landscapeImageUrl, String contentRating) - update movie by title.

     */

    @PutMapping("/update/{targetMovieTitle}")
    public String UpdateMovie(@RequestBody String json, @PathVariable String targetMovieTitle)
    {
        System.out.println("MovieUpdateController.UpdateMovie() is called!");
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);

            movieImpl.updateMovie(
                    targetMovieTitle,
                    jsonNode.get("title").asText(),
                    jsonNode.get("genre").asText(),
                    jsonNode.get("description").asText(),
                    LocalDate.parse(jsonNode.get("releaseDate").asText()),
                    jsonNode.get("imageUrl").asText(),
                    jsonNode.get("landscapeImageUrl").asText(),
                    jsonNode.get("contentRating").asText()
            );

            return "Success update movie";

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

}
