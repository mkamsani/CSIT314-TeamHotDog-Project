package com.hotdog.ctbs.controller.manager;

// Application imports.
import com.hotdog.ctbs.service.implementation.MovieImpl;

// Spring imports.
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/manager/movie")
public class MovieSuspendController {

    private final MovieImpl movieImpl;

    public MovieSuspendController(MovieImpl movieImpl)
    {
        this.movieImpl = movieImpl;
    }

    /*
     MovieSuspendController
     Method will be used:
        suspendMovie(String targetMovieTitle) - suspend movie by title.
     */

    @DeleteMapping("/suspend/{targetMovieTitle}")
    public String SuspendMovie(@PathVariable String targetMovieTitle) {

        try {
            movieImpl.suspendMovie(targetMovieTitle);
            return "Success deleted movie.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }

    }

}
