package com.hotdog.ctbs.controller.customer;

import com.hotdog.ctbs.entity.Movie;
import com.hotdog.ctbs.repository.MovieRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/customer/movie")
public class CustomerMovieReadController {

    private final MovieRepository movieRepo;

    public CustomerMovieReadController(MovieRepository movieRepo)
    {
        this.movieRepo = movieRepo;
    }

    // curl -X GET http://localhost:8000/api/customer/movie/read/active
    // inside local
    // windows cmd: curl.exe -X GET http://localhost:8000/api/customer/movie/read/active
    @GetMapping(value = "/read/{param}")
    public ResponseEntity<String> CustomerReadMovie(@PathVariable final String param)
    {
        try {
            return ResponseEntity.ok().body(Movie.readMovieCustomer(movieRepo, param));
        } catch (Exception e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
