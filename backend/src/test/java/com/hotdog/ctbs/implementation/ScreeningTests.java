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

    }

    @Test
    void violateImpl(){

        // can perform any test to violate the createScreening method
        System.out.println("violate the implementation");
        screeningImpl.createScreening("Ultraman", "midnight",
                true, LocalDate.of(2025, 5, 1), 1);

    }

    @Test
    void getter(){

        // test getAllScreenings
        System.out.println("Test getAllScreenings()");
        System.out.println(screeningImpl.getAllScreenings());
        System.out.println();

    }


}

