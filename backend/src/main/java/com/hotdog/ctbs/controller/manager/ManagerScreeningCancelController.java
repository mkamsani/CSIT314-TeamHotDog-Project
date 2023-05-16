package com.hotdog.ctbs.controller.manager;

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
import com.hotdog.ctbs.service.implementation.ScreeningImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/manager/screening")
public class ManagerScreeningCancelController {

    private final ScreeningImpl screeningImpl;

    public ManagerScreeningCancelController(ScreeningImpl screeningImpl)
    {
        this.screeningImpl = screeningImpl;
    }



    // curl.exe -X DELETE http://localhost:8000/api/manager/screening/10:00/2021-05-01/1
    @PutMapping("/cancel/{currentShowTime}/{currentShowDate}/{cinemaRoomId}")
    public String CancelMovie(@PathVariable String currentShowTime,
                               @PathVariable String currentShowDate,
                               @PathVariable Integer cinemaRoomId) {

        try {
            screeningImpl.cancelScreening(currentShowTime, LocalDate.parse(currentShowDate), cinemaRoomId);
            return "Successfully cancel the screening.";

        } catch (Exception e) {
            return e.getMessage();
        }

    }
}
