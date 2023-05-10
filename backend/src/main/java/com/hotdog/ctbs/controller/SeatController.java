package com.hotdog.ctbs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotdog.ctbs.service.implementation.SeatImpl;
import net.datafaker.formats.Json;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seat")
public class SeatController {
    private final SeatImpl seatImpl;

    public SeatController(SeatImpl seatImpl) {
        this.seatImpl = seatImpl;
    }

    @GetMapping("/read/getSeatByRowAndColumnAndCinemaRoomId")
    public String ReadSeatByRowAndColumnAndCinemaRoomId(@RequestBody String json) throws JsonProcessingException {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            char row = jsonNode.get("row").asText().charAt(0);
            Integer column = jsonNode.get("column").asInt();
            Integer cinemaRoomId = jsonNode.get("cinemaRoomId").asInt();
            return seatImpl.getSeatByRowAndColumnAndCinemaRoomId(row, column, cinemaRoomId).toString();
        }
        catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
