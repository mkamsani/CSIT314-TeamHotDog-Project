package com.hotdog.ctbs.controller.manager;

// Application imports.
import com.hotdog.ctbs.service.implementation.ScreeningImpl;

// Spring imports.
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/manager/screening")
public class ScreeningSuspendController{

    private final ScreeningImpl screeningImpl;

    public ScreeningSuspendController(ScreeningImpl screeningImpl)
    {
        this.screeningImpl = screeningImpl;
    }



    // curl.exe -X DELETE http://localhost:8000/api/manager/screening/suspend/I%20am%20Number%20Four/10:00/2021-05-01/1
    @DeleteMapping("/suspend/{targetMovieTitle}/{currentShowTime}/{currentShowDate}/{cinemaRoomId}")
    public String SuspendMovie(@PathVariable String targetMovieTitle,
                                @PathVariable String currentShowTime,
                                @PathVariable String currentShowDate,
                                @PathVariable Integer cinemaRoomId) {

        try {
            screeningImpl.suspendScreening(targetMovieTitle, currentShowTime, LocalDate.parse(currentShowDate), cinemaRoomId);
            return "Success suspend screening.";

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }

    }

}
