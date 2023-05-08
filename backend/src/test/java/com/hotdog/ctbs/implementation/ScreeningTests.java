package com.hotdog.ctbs.implementation;


import com.hotdog.ctbs.entity.Screening;
import com.hotdog.ctbs.entity.Movie;
import com.hotdog.ctbs.entity.CinemaRoom;
import com.hotdog.ctbs.repository.CinemaRoomRepository;
import com.hotdog.ctbs.repository.MovieRepository;
import com.hotdog.ctbs.service.ScreeningService;

import com.hotdog.ctbs.service.implementation.ScreeningImpl;
import com.hotdog.ctbs.service.implementation.MovieImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest
public class ScreeningTests {

    @Autowired
    private ScreeningImpl screeningImpl;
    @Autowired
    private CinemaRoomRepository cinemaRoomRepository;


    @Test
    void createMethod(){

        System.out.println("Testing createScreening method");
        // date to be 2025-05-01
        screeningImpl.createScreening("Spider-Man", "morning",
                true, LocalDate.of(2025, 5, 1), 1);
        // create another different
        screeningImpl.createScreening("Spider-Man", "afternoon",
                true, LocalDate.of(2025, 5, 1), 1);

        screeningImpl.createScreening("Inception", "evening",
                true, LocalDate.of(2025, 5, 1), 1);

        screeningImpl.createScreening("Inception", "midnight",
                true, LocalDate.of(2025, 5, 1), 1);

        // Attempt to violate the createScreening method
        // Why surround in a try-catch block?
        // Because the violation is expected, and it should not fail the test in a CI/CD pipeline.
        try {
            screeningImpl.createScreening("Ultraman", "midnight",
                                          true, LocalDate.of(2025, 5, 1), 1);
        } catch (Exception e) {
            Assertions.assertEquals(
                    e.getMessage(),
                    // Update this, if you update the createScreening method's exception message!!!
                    "Cinema room is already full for the given date.",

                    // The message below is printed, if the test fails.
                    "Did not receive the expected exception: \"Cinema room is already full for the given date.\"");
        }
    }

    @Test
    void getter(){
        // test getAllScreenings
        System.out.println("Test getAllScreenings()");
        System.out.println(screeningImpl.getAllScreenings());
        System.out.println();
    }


}

