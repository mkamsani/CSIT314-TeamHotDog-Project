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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/manager/movie")
public class ManagerMovieReadController {

    private final MovieImpl movieImpl;
    private final ObjectMapper objectMapper;

    public ManagerMovieReadController(MovieImpl movieImpl)
    {
        this.movieImpl = movieImpl;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    /*
     ManagerMovieReadController
     Method will be used:
     getAllMoviesDetails() - returns all movies.
     getMovieByTitle(String title) - returns movie by title.
     */

    // curl.exe -X GET http://localhost:8000/api/manager/movie/read/all
    // curl.exe -X GET http://localhost:8000/api/manager/movie/read/I%20am%20Number%20Four
    // %20 is space.
    @GetMapping(value = "/read/{param}")
    public String ManagerReadMovie(@PathVariable final String param)
    {
        try {
            switch (param) {

                case "all" -> {
                    return movieImpl.getAllMoviesDetails().toString();
                }
                default -> {
                    return String.valueOf(movieImpl.getMovieByTitle(param));
                }

            }

        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
