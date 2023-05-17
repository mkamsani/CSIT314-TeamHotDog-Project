package com.hotdog.ctbs.controller.manager;

// Application imports.
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotdog.ctbs.entity.Movie;
import com.hotdog.ctbs.repository.MovieRepository;

// Spring imports.
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/manager/movie")
public class ManagerMovieSuspendController {

    private final MovieRepository movieRepo;
    private final ObjectMapper objectMapper;

    public ManagerMovieSuspendController(MovieRepository movieRepo)
    {
        this.movieRepo = movieRepo;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }


    // curl.exe -X DELETE http://localhost:8000/api/manager/movie/suspend/I%20am%20Number%20Four
    // %20 is space.
    @DeleteMapping("/suspend/{targetMovieTitle}")
    public ResponseEntity<String> Suspend(@PathVariable String targetMovieTitle) {

        try {
            Movie.suspendMovie(movieRepo, targetMovieTitle);
            return ResponseEntity.ok("Successfully suspend movie: " + targetMovieTitle);
        }
        catch (Exception e){

            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


}
