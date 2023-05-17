package com.hotdog.ctbs.controller.customer;

// Application imports.
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}
