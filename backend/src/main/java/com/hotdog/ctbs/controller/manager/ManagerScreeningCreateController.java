package com.hotdog.ctbs.controller.manager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotdog.ctbs.entity.Screening;
import com.hotdog.ctbs.repository.CinemaRoomRepository;
import com.hotdog.ctbs.repository.MovieRepository;
import com.hotdog.ctbs.repository.ScreeningRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/manager/screening")
public class ManagerScreeningCreateController {

    private final ScreeningRepository screeningRepo;
    private final MovieRepository movieRepo;

    private final CinemaRoomRepository cinemaRoomRepo;
    private final ObjectMapper objectMapper;

    public ManagerScreeningCreateController(MovieRepository movieRepo,
                                            ScreeningRepository screeningRep,
                                            CinemaRoomRepository cinemaRoomRepo)
    {
        this.movieRepo = movieRepo;
        this.screeningRepo = screeningRep;
        this.cinemaRoomRepo = cinemaRoomRepo;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    // Invoke-WebRequest -Method POST -Headers @{"Content-Type"="application/json"} -Body '{"MovieTitle":"Thor","ShowTime":"morning","ShowDate":"2026-05-31","CinemaRoomId":1}' -Uri http://localhost:8000/api/manager/screening/create/screening
    @PostMapping("/create/screening")
    public ResponseEntity<String> Create(@RequestBody final String json)
    {
        System.out.println("ScreeningCreateController.CreateScreening()");
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            Screening.createScreening(
                    movieRepo,
                    screeningRepo,
                    cinemaRoomRepo,
                    jsonNode.get("MovieTitle").asText(),
                    jsonNode.get("ShowTime").asText(),
                    LocalDate.parse(jsonNode.get("ShowDate").asText()),
                    jsonNode.get("CinemaRoomId").asInt()
            );
            return ResponseEntity.ok("Successfully created screening.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
