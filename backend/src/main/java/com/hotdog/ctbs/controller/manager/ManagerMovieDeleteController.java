package com.hotdog.ctbs.controller.manager;

import com.hotdog.ctbs.entity.Movie;
import com.hotdog.ctbs.repository.MovieRepository;
import com.hotdog.ctbs.repository.ScreeningRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/manager/movie")
public class ManagerMovieDeleteController {

    private final MovieRepository movieRepo;
    private final ScreeningRepository screeningRepo;

    public ManagerMovieDeleteController(MovieRepository movieRepo, ScreeningRepository screeningRepo)
    {
        this.movieRepo = movieRepo;
        this.screeningRepo = screeningRepo;
    }

    // curl.exe -X DELETE http://localhost:8000/api/manager/movie/delete/I%20am%20Number%20Four
    @DeleteMapping("/delete/{targetMovieTitle}")
    public ResponseEntity<String> Delete(@PathVariable String targetMovieTitle)
    {

        try {
            Movie.deleteMovie(movieRepo, screeningRepo, targetMovieTitle);
            return ResponseEntity.ok().body("Successfully delete movie: " + targetMovieTitle);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


}
