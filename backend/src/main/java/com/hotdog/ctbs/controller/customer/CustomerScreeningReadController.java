package com.hotdog.ctbs.controller.customer;

import com.hotdog.ctbs.entity.Screening;
import com.hotdog.ctbs.repository.MovieRepository;
import com.hotdog.ctbs.repository.ScreeningRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/customer/screening")
public class CustomerScreeningReadController {

    private final ScreeningRepository screeningRepo;
    private final MovieRepository movieRepo;

    public CustomerScreeningReadController(ScreeningRepository screeningRepo,
                                           MovieRepository movieRepo)
    {
        this.screeningRepo = screeningRepo;
        this.movieRepo = movieRepo;
    }

    @GetMapping(value = "/read/{param}")
    public ResponseEntity<String> Read(@PathVariable final String param)
    {
        try {
            String json = Screening.readScreeningCustomer(screeningRepo, movieRepo, param);
            return ResponseEntity.ok().body(json);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
