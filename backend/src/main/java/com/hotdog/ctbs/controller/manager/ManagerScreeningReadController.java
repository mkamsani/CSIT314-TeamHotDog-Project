package com.hotdog.ctbs.controller.manager;

// Application imports.

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotdog.ctbs.entity.Screening;
import com.hotdog.ctbs.service.implementation.ScreeningImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/manager/screening")
public class ManagerScreeningReadController {

    private final ScreeningImpl screeningImpl;
    private final ObjectMapper objectMapper;

    public ManagerScreeningReadController(ScreeningImpl screeningImpl)
    {
        this.screeningImpl = screeningImpl;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    /*
        ManagerScreeningReadController
        Method will be used:
        getAllScreeningsDetails() - returns all screenings.
        getScreeningByTitle(String title) - returns screening by title.

     */

    // curl.exe -X GET http://localhost:8000/api/manager/screening/read/all
    // curl.exe -X GET http://localhost:8000/api/manager/screening/read/Thor
    @GetMapping(value = "/read/{param}")
    public String ManagerReadScreening(@PathVariable final String param)
    {
        try {
            switch (param) {

                case "all" -> {
                        return screeningImpl.getAllScreenings().toString();
                }

                default -> {
                    return String.valueOf(screeningImpl.getAllScreeningsByMovieTitle(param));

                }

            }

        } catch (Exception e) {
            return e.getMessage();
        }

    }

    /*// 2 parameters, the second one is optional.
    // Bit of a hack, but everything is in one method. Not necessarily more readable.
    @GetMapping(value = "/read/{param}/{cinemaRoomID}")
    public String Read(@PathVariable(name = "param") final String param,
                       @PathVariable(name = "cinemaRoomID", required = false) final Optional<String> cinemaRoomID)
    {
        try {
            List<Screening> screeningList = switch (param) {
                case "all" -> screeningImpl.getAllScreenings();
                case "active" -> screeningImpl.getAllActiveScreenings();
                case "cinemaRoomID" -> {
                    if (cinemaRoomID.isPresent() && cinemaRoomID.get().matches("[0-9]+"))
                        yield screeningImpl.getAllScreeningsByCinemaRoomId(Integer.parseInt(cinemaRoomID.get()));
                    throw new Exception("Invalid cinema room ID.");
                }
                default -> screeningImpl.getAllScreeningsByMovieTitle(param);
            };
            return String.valueOf(screeningList);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }*/

    // Most correct way: logic is split.
    // @GetMapping(value = "/read/fixed/{param}")
    // @GetMapping(value = "/read/cinema-room-id/{param}")
    // @GetMapping(value = "/read/movie-title/{param}")
    // @GetMapping(value = "/read/date/{param}")
}
