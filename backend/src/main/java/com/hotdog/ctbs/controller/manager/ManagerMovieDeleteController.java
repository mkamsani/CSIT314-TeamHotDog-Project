package com.hotdog.ctbs.controller.manager;

// Application imports.
import com.hotdog.ctbs.entity.Movie;
import com.hotdog.ctbs.service.implementation.MovieImpl;

// Java imports.
import java.util.List;

// JSON serialization imports.
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// Spring imports.
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/manager/movie")
public class ManagerMovieDeleteController {

    private final MovieImpl movieImpl;
    private final ObjectMapper objectMapper;

    public ManagerMovieDeleteController(MovieImpl movieImpl)
    {
        this.movieImpl = movieImpl;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    /*
     ManagerMovieDeleteController
     Method will be used:
        deleteMovieByTitle(String targetMovieTitle) - delete movie by title.
     */

    // curl.exe -X DELETE http://localhost:8000/api/manager/movie/delete/I%20am%20Number%20Four
    @DeleteMapping("/delete/{targetMovieTitle}")
    public String DeleteMovie(@PathVariable String targetMovieTitle)
    {
        try {
            movieImpl.deleteMovieByTitle(targetMovieTitle);
            return "Success delete movie.";
        } catch (Exception e) {
            return e.getMessage();
        }
    }



}
