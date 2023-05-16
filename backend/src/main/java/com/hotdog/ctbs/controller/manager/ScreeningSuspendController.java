package com.hotdog.ctbs.controller.manager;

// Application imports.
import com.hotdog.ctbs.service.implementation.ScreeningImpl;

// Spring imports.
import org.springframework.web.bind.annotation.*;

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



    // curl.exe -X DELETE http://localhost:8000/api/manager/screening/suspend/10:00/2021-05-01/1
    @PutMapping("/suspend/{currentShowTime}/{currentShowDate}/{cinemaRoomId}")
    public String SuspendMovie(@PathVariable String currentShowTime,
                                @PathVariable String currentShowDate,
                                @PathVariable Integer cinemaRoomId) {

        try {
            screeningImpl.suspendScreening(currentShowTime, LocalDate.parse(currentShowDate), cinemaRoomId);
            return "Success suspend screening.";

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }

    }

}
