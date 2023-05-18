package com.hotdog.ctbs.controller.customer;

import com.hotdog.ctbs.entity.Screening;
import com.hotdog.ctbs.repository.ScreeningRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/customer/screening/seats")
public class CustomerScreeningSeatsReadController {

    private final ScreeningRepository screeningRepo;

    public CustomerScreeningSeatsReadController(ScreeningRepository screeningRepo)
    {
        this.screeningRepo = screeningRepo;
    }

    @GetMapping(value = "/read/{date}/{time}/{room}")
    public ResponseEntity<String> Read(@PathVariable final String date,
                                       @PathVariable final String time,
                                       @PathVariable final String room)
    {
        try {
            String json = Screening.readScreeningSeatsCustomer(
                    screeningRepo,
                    LocalDate.parse(date),
                    time,
                    Integer.parseInt(room)
            );
            return ResponseEntity.ok().body(json);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}