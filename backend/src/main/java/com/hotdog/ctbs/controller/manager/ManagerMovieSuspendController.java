package com.hotdog.ctbs.controller.manager;

import com.hotdog.ctbs.entity.Movie;
import com.hotdog.ctbs.repository.MovieRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/manager/movie")
public class ManagerMovieSuspendController {

    private final MovieRepository movieRepo;

    public ManagerMovieSuspendController(MovieRepository movieRepo)
    {
        this.movieRepo = movieRepo;
    }


    // curl.exe -X DELETE http://localhost:8000/api/manager/movie/suspend/I%20am%20Number%20Four
    // %20 is space.
    @DeleteMapping("/suspend/{targetMovieTitle}")
    public ResponseEntity<String> Suspend(@PathVariable String targetMovieTitle)
    {

        try {
            Movie.suspendMovie(movieRepo, targetMovieTitle);
            return ResponseEntity.ok("Successfully suspend movie: " + targetMovieTitle);
        } catch (Exception e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


}
