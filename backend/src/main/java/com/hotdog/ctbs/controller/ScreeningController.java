package com.hotdog.ctbs.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotdog.ctbs.service.implementation.ScreeningImpl;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/screening")
public class ScreeningController {

    private final ScreeningImpl screeningImpl;

    public ScreeningController(ScreeningImpl screeningImpl)
    {
        this.screeningImpl = screeningImpl;
    }

    // 4 Basic CRUD operations (delete will be replaced by suspend)
    // 1. create a new movie
    // Note : must use valid movie title (can get a list from ReadAllActiveMovieTitles() from MovieController)
    @PostMapping("/create/screening")
    public String CreateScreening(@RequestBody String json)
    {
        System.out.println("ScreeningController.CreateScreening method() is called.");

        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String movieTitle = jsonNode.get("movieTitle").asText();
            String showTime = jsonNode.get("showTime").asText();
            LocalDate showDate = LocalDate.parse(jsonNode.get("showDate").asText());
            Integer cinemaRoomId = jsonNode.get("cinemaRoomId").asInt();

            screeningImpl.createScreening(movieTitle, showTime,
                    showDate, cinemaRoomId);
            return "Screening created successfully";

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // 2. read the screening that are later than or equal to now time
    // (has "2" methods for different purpose)
    // the result will be sort follow this ascending order :
    //1st order ==> show date in ascending order
    // 2nd order ==> show time in ascending order
    // 3rd order ==> cinema room in ascending order
    // 4th order ==> movie in ascending order

    // 2.1 To return a list of all screening objects (including active and inactive screening) for "MANAGER" to view
    @GetMapping("/read/allScreeningsDetails")
    public String ReadAllMoviesDetails()
    {
        System.out.println("ScreeningController.ReadAllMoviesDetails method() is called.");

        return screeningImpl.getAllScreenings().toString();

    }

    // 2.2 To return a list of all active screening objects for "CUSTOMER" to view
    @GetMapping("/read/allActiveScreeningsDetails")
    public String ReadAllActiveMoviesDetails()
    {
        System.out.println("ScreeningController.ReadAllActiveMoviesDetails method() is called.");

        return screeningImpl.getAllActiveScreenings().toString();

    }

    // 3. update screening
    // *** can update all attribute of a screening except the "isActive" (status) ==> (suspend should be in point 4)
    // update a screening require all 4 fields (movieTitle, showTime, showDate, cinemaRoomId)
    // if a screening is inactive or passed, cant update
    @PutMapping("/update/screening")
    public String UpdateScreening(@RequestBody String json)
    {
        System.out.println("ScreeningController.UpdateScreening method() is called.");

        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String currentMovieTitle = jsonNode.get("currentMovieTitle").asText();
            String currentShowTime = jsonNode.get("currentShowTime").asText();
            LocalDate currentShowDate = LocalDate.parse(jsonNode.get("currentShowDate").asText());
            Integer currentCinemaRoomId = jsonNode.get("currentCinemaRoomId").asInt();

            String newMovieTitle = jsonNode.get("newMovieTitle").asText();
            String newShowTime = jsonNode.get("newShowTime").asText();
            LocalDate newShowDate = LocalDate.parse(jsonNode.get("newShowDate").asText());
            Integer newCinemaRoomId = jsonNode.get("newCinemaRoomId").asInt();

            screeningImpl.updateScreening(currentMovieTitle, currentShowTime,
                    currentShowDate, currentCinemaRoomId,
                    newMovieTitle, newShowTime, newShowDate, newCinemaRoomId);
            return "Screening updated successfully";

        } catch (Exception e) {

            return "Error: " + e.getMessage();
        }
    }

    // 4. suspend screening
    // suspend a screening require 5 fields
    //  4 fields == (movieTitle, showTime, showDate, cinemaRoomId) to identify a screening
    // another field == "newIsActive" to indicate the new status of the screening which is false
    // *** can only suspend an active screening
    // *** cannot reactivate a suspended screening ***
    // *** cannot suspend a past screening ***
    @PutMapping("/suspend/screening")
    public String SuspendScreening(@RequestBody String json)
    {
        System.out.println("ScreeningController.SuspendScreening method() is called.");

        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String movieTitle = jsonNode.get("movieTitle").asText();
            String showTime = jsonNode.get("showTime").asText();
            LocalDate showDate = LocalDate.parse(jsonNode.get("showDate").asText());
            Integer cinemaRoomId = jsonNode.get("cinemaRoomId").asInt();
            Boolean newIsActive = jsonNode.get("newIsActive").asBoolean();

            screeningImpl.suspendScreeningByIsActive(movieTitle, showTime,
                    showDate, cinemaRoomId, newIsActive);

            return "Screening suspended successfully";

        } catch (Exception e) {

            return "Error: " + e.getMessage();
        }
    }

}
