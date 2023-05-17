package com.hotdog.ctbs.controller.manager;

// Application imports.
import com.hotdog.ctbs.entity.Movie;
import com.hotdog.ctbs.repository.MovieRepository;

// Java imports.
import java.time.LocalDate;

// JSON deserialization imports.
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// Spring imports.
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/manager/movie")
public class ManagerMovieUpdateController {

    private final MovieRepository movieRepo;
    private final ObjectMapper objectMapper;

    public ManagerMovieUpdateController(MovieRepository movieRepo)
    {
        this.movieRepo = movieRepo;

        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    // Invoke-WebRequest -Method PUT -Uri 'http://localhost:8000/api/manager/movie/update/Matrix' -Headers @{'Content-Type'='application/json'} -Body '{"title":"XXXXX","genre":"Action","description":"A computer hacker learns from mysterious rebels about the true nature of his controllers.","releaseDate":"1999-03-31","imageUrl":"https://www.themoviedb.org/t/p/w600_and_h900_bestv2/hEpWvX6Bp79eLxY1kX5ZZJcme5U.jpg","landscapeImageUrl":"https://www.themoviedb.org/t/p/w1920/3KN24PrOheHVYs9ypuOIdFBEpX.jpg","contentRating":"pg13"}'
    @PutMapping("/update/{targetMovieTitle}")
    public ResponseEntity<String> Update(@RequestBody String json, @PathVariable String targetMovieTitle)
    {
        System.out.println("MovieUpdateController.UpdateMovie() is called!");
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);

            Movie.updateMovie(
                    movieRepo,
                    targetMovieTitle,
                    jsonNode.get("title").asText(),
                    jsonNode.get("genre").asText(),
                    jsonNode.get("description").asText(),
                    LocalDate.parse(jsonNode.get("releaseDate").asText()),
                    jsonNode.get("imageUrl").asText(),
                    jsonNode.get("landscapeImageUrl").asText(),
                    jsonNode.get("contentRating").asText()
            );

            // return the movie title message that has been updated.
            return ResponseEntity.ok("Successfully update from " + targetMovieTitle  + " to " + jsonNode.get("title").asText());

        } catch (Exception e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
