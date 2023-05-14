package com.hotdog.ctbs.controller.customer;

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
@RequestMapping("/customer/movie")
public class CustomerMovieReadController {

    private final MovieImpl movieImpl;
    private final ObjectMapper objectMapper;

    public CustomerMovieReadController(MovieImpl movieImpl)
    {
        this.movieImpl = movieImpl;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    /*
     CustomerMovieReadController
     Method will be used:
     getAllActiveMoviesDetails() - returns all active movies.
     getActiveMovieByTitle(String title) - returns active movie by title.
     */

    // curl -X GET http://localhost:8000/api/customer/movie/read/active
    // inside local
    // windows cmd: curl.exe -X GET http://localhost:8000/api/customer/movie/read/active
    @GetMapping(value = "/read/{param}")
    public String CustomerReadMovie(@PathVariable final String param)
    {
        try {
            switch (param) {

                case "active" -> {
                    return movieImpl.getAllActiveMoviesDetails().toString();
                }
                default -> {
                    return String.valueOf(movieImpl.getActiveMovieByTitle(param));
                }

            }

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

}
