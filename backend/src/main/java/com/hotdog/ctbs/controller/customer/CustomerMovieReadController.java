package com.hotdog.ctbs.controller.customer;

// Application imports.
import com.hotdog.ctbs.entity.Movie;
import com.hotdog.ctbs.repository.MovieRepository;

// Java imports.

// JSON serialization imports.
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// Spring imports.
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/customer/movie")
public class CustomerMovieReadController {

    private final MovieRepository movieRepo;
    private final ObjectMapper objectMapper;

    public CustomerMovieReadController(MovieRepository movieRepo)
    {
        this.movieRepo = movieRepo;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    // curl -X GET http://localhost:8000/api/customer/movie/read/active
    // inside local
    // windows cmd: curl.exe -X GET http://localhost:8000/api/customer/movie/read/active
    @GetMapping(value = "/read/{param}")
    public ResponseEntity<String> CustomerReadMovie(@PathVariable final String param)
    {
            try {
                return ResponseEntity.ok().body(Movie.readActiveMovie(movieRepo, param));
            }
            catch (Exception e){

                return ResponseEntity.badRequest().body(e.getMessage());
            }

    }


}
