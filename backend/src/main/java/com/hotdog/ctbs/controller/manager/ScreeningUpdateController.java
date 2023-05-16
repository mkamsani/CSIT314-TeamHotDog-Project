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
import com.hotdog.ctbs.service.implementation.UserAccountImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/manager/screening")
public class ScreeningUpdateController {

    private final ScreeningImpl screeningImpl;
    private final ObjectMapper objectMapper;

    public ScreeningUpdateController(ScreeningImpl screeningImpl)
    {
        this.screeningImpl = screeningImpl;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }


    // update a screening using curl
    // Invoke-WebRequest -Method PUT -Headers @{"Content-Type"="application/json"} -Body '{"newMovieTitle":"Up","newShowTime":"midnight","newShowDate":"2026-05-25","newCinemaRoomId":1}' -Uri http://localhost:8000/api/manager/screening/update/afternoon/2026-05-31/1
    @PutMapping("/update/{targetShowTime}/{targetShowDate}/{targetCinemaRoomId}")
    public String UpdateScreening(@RequestBody String json, @PathVariable String targetShowTime,
                                  @PathVariable LocalDate targetShowDate, @PathVariable Integer targetCinemaRoomId)
    {
        System.out.println("ScreeningUpdateController.UpdateScreening() called.");
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            screeningImpl.updateScreening(targetShowTime,
                    targetShowDate, targetCinemaRoomId,
                    jsonNode.get("newMovieTitle").asText(),
                    jsonNode.get("newShowTime").asText(),
                    LocalDate.parse(jsonNode.get("newShowDate").asText()),
                    jsonNode.get("newCinemaRoomId").asInt()

            );
            return "Successfully update screening.";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
