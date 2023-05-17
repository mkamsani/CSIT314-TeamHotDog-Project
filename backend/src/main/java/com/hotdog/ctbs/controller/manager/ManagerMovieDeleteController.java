package com.hotdog.ctbs.controller.manager;

// Application imports.
import com.hotdog.ctbs.entity.Movie;
import com.hotdog.ctbs.entity.Screening;
import com.hotdog.ctbs.repository.MovieRepository;
import com.hotdog.ctbs.repository.ScreeningRepository;


// Java imports.
import java.time.LocalDate;
import java.util.List;

// JSON serialization imports.
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// Spring imports.
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/manager/movie")
public class ManagerMovieDeleteController {

    private final MovieRepository movieRepo;

    private final ScreeningRepository screeningRepo;
    private final ObjectMapper objectMapper;

    public ManagerMovieDeleteController(MovieRepository movieRepo, ScreeningRepository screeningRepo)
    {
        this.movieRepo = movieRepo;
        this.screeningRepo = screeningRepo;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
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
