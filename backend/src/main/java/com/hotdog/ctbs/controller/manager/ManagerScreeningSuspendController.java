package com.hotdog.ctbs.controller.manager;

import com.hotdog.ctbs.entity.Screening;
import com.hotdog.ctbs.repository.ScreeningRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/manager/screening")
public class ManagerScreeningSuspendController {

    private final ScreeningRepository screeningRepo;

    public ManagerScreeningSuspendController(ScreeningRepository screeningRepo)
    {
        this.screeningRepo = screeningRepo;
    }

    // curl.exe -X DELETE http://localhost:8000/api/manager/screening/suspend/10:00/2021-05-01/1
    @PutMapping("/suspend/{currentShowTime}/{currentShowDate}/{cinemaRoomId}")
    public ResponseEntity<String> Suspend(@PathVariable String currentShowTime,
                                          @PathVariable String currentShowDate,
                                          @PathVariable Integer cinemaRoomId) {
        try {
            Screening.suspendScreening(
                    screeningRepo,
                    currentShowTime,
                    LocalDate.parse(currentShowDate),
                    cinemaRoomId
            );
            return ResponseEntity.ok().body("Successfully suspend screening.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}
