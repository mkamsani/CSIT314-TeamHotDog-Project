package com.hotdog.ctbs.controller.manager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotdog.ctbs.entity.Movie;
import com.hotdog.ctbs.repository.MovieRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/manager/movie")
public class ManagerMovieCreateController {

    private final MovieRepository movieRepo;
    private final ObjectMapper objectMapper;

    public ManagerMovieCreateController(MovieRepository movieRepo)
    {
        this.movieRepo = movieRepo;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }
    
    // create a new movie
    // Invoke-WebRequest -Method POST -Headers @{"Content-Type"="application/json"} -Body '{"title":"The Haha","genre":"Action","description":"A computer hacker learns from mysterious rebels about the true nature of his controllers.","releaseDate":"1999-03-31","imageUrl":"https://www.themoviedb.org/t/p/w600_and_h900_bestv2/hEpWvX6Bp79eLxY1kX5ZZJcme5U.jpg","landscapeImageUrl":"https://www.themoviedb.org/t/p/w1920/3KN24PrOheHVYs9ypuOIdFBEpX.jpg","contentRating":"pg13"}' -Uri http://localhost:8000/api/manager/movie/create/movie
    @PostMapping("/create/movie")
    public ResponseEntity<String> Create(@RequestBody final String json)
    {
        System.out.println("ManagerMovieCreateController.Create() called.");
        try {
            JsonNode jsonNode = objectMapper.readTree(json);

            Movie.createMovie(
                    movieRepo,
                    jsonNode.get("title").asText(),
                    jsonNode.get("genre").asText(),
                    jsonNode.get("description").asText(),
                    LocalDate.parse(jsonNode.get("releaseDate").asText()),
                    jsonNode.get("imageUrl").asText(),
                    jsonNode.get("landscapeImageUrl").asText(),
                    jsonNode.get("contentRating").asText()

            );

            return ResponseEntity.ok("Successfully create movie: " + jsonNode.get("title").asText());

        } catch (Exception e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}
