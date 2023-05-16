package com.hotdog.ctbs.controller.manager;

// Application imports.
import com.hotdog.ctbs.service.implementation.ScreeningImpl;

// Java imports.
import java.time.LocalDate;

// JSON deserialization imports.
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// Spring imports.
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/manager/screening")
public class ScreeningCreateController {

    private final ScreeningImpl screeningImpl;
    private final ObjectMapper objectMapper;

    public ScreeningCreateController(ScreeningImpl screeningImpl)
    {
        this.screeningImpl = screeningImpl;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }


    /*
        ScreeningCreateController
        Method will be used:
            createScreening(String targetMovieTitle, String targetShowTime,
            LocalDate targetShowDate, Integer targetCinemaRoomId) - create screening.

     */

    // create a new screening
    // Invoke-WebRequest -Method POST -Headers @{"Content-Type"="application/json"} -Body '{"MovieTitle":"Thor","ShowTime":"morning","ShowDate":"2026-05-31","CinemaRoomId":1}' -Uri http://localhost:8000/api/manager/screening/create/screening
    @PostMapping("/create/screening")
    public String CreateScreening(@RequestBody final String json)
    {
        System.out.println("ScreeningCreateController.CreateScreening()");

        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            screeningImpl.createScreening(
                    jsonNode.get("MovieTitle").asText(),
                    jsonNode.get("ShowTime").asText(),
                    LocalDate.parse(jsonNode.get("ShowDate").asText()),
                    jsonNode.get("CinemaRoomId").asInt()

            );
            return "Screening was created successfully.";

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

}
