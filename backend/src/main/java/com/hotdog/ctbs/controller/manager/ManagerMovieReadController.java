package com.hotdog.ctbs.controller.manager;

import com.hotdog.ctbs.entity.Movie;
import com.hotdog.ctbs.repository.MovieRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/manager/movie")
public class ManagerMovieReadController {

    private final MovieRepository movieRepo;

    public ManagerMovieReadController(MovieRepository movieRepo)
    {
        this.movieRepo = movieRepo;
    }

    // curl.exe -X GET http://localhost:8000/api/manager/movie/read/all
    // curl.exe -X GET http://localhost:8000/api/manager/movie/read/I%20am%20Number%20Four
    // %20 is space.
    @GetMapping(value = "/read/{param}")
    public ResponseEntity<String> Read(@PathVariable final String param)
    {
        try {
            return ResponseEntity.ok().body(Movie.readMovieManager(movieRepo, param));
        } catch (Exception e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
