package com.hotdog.ctbs.controller.manager;

// Java imports.
import java.time.LocalDate;

// Spring imports.
import com.hotdog.ctbs.repository.ScreeningRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/manager/screening")
public class ManagerScreeningCancelController {

    private final ScreeningRepository screeningRepo;

    public ManagerScreeningCancelController(ScreeningRepository screeningRepo)
    {
        this.screeningRepo = screeningRepo;
    }

    @PutMapping("/cancel/{currentShowTime}/{currentShowDate}/{cinemaRoomId}")
    public ResponseEntity<String> CancelMovie(@PathVariable String currentShowTime,
                                              @PathVariable String currentShowDate,
                                              @PathVariable Integer cinemaRoomId) {
        try {
            // TODO: replace with Screening.cancelScreening IF WE HAVE THE TIME.
            //  screeningImpl.cancelScreening(screeningRepo,
            //                                currentShowTime,
            //                                LocalDate.parse(currentShowDate),
            //                                cinemaRoomId
            //  );
            return ResponseEntity.ok("Successfully cancel the screening.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
