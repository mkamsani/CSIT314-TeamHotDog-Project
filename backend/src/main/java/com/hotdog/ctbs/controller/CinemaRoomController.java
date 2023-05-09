package com.hotdog.ctbs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import com.hotdog.ctbs.service.implementation.CinemaRoomImpl;

@RestController
@RequestMapping("/cinemaRoom")
public class CinemaRoomController {
    private final CinemaRoomImpl cinemaRoomImpl;
    public CinemaRoomController(CinemaRoomImpl cinemaRoomImpl)
    {
        this.cinemaRoomImpl = cinemaRoomImpl;
    }

    // To return a list of all cinema room ids
    @GetMapping("/read/allCinemaRoomIds")
    public String ReadAllCinemaRoomIds()
    {
        return cinemaRoomImpl.getAllCinemaRoomIds().toString();
    }

    // To return a list of all cinema room objects
    @GetMapping("/read/allCinemaRoomDetails")
    public String ReadAllCinemaRoomDetails()
    {
        return cinemaRoomImpl.getAllCinemaRoom().toString();
    }

    // To return a list of all "active" cinema room ids
    @GetMapping("/read/allActiveCinemaRoomIds")
    public String ReadAllActiveCinemaRoomIds()
    {
        return cinemaRoomImpl.getAllActiveCinemaRooms().toString();
    }

    // To return an individual cinema room object
    @GetMapping("/read/cinemaRoomDetails")
    public String ReadCinemaRoomDetails(@RequestBody String json) {
        try{
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            Integer targetID = jsonNode.get("targetId").asInt();
            return cinemaRoomImpl.getCinemaRoomById(targetID).toString();
        }
        catch(Exception e){
            return "Invalid input";
        }
    }

    // Update cinema room status
    @PutMapping("/update/cinemaRoomSuspension")
    public String suspendCinemaRoom(@RequestBody String json) {
        try{
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            Integer targetID = jsonNode.get("targetId").asInt();
            Boolean newStatus = jsonNode.get("newStatus").asBoolean();
            if (newStatus == false){
                cinemaRoomImpl.updateCinemaRoom(targetID, newStatus);
                return "Cinema room status updated";
            }
            else{
                return "Cinema room status cannot be updated to true";
            }
        }
        catch(Exception e){
            return "Invalid input";
        }
    }
}
