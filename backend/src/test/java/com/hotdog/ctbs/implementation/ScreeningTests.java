package com.hotdog.ctbs.implementation;


import com.hotdog.ctbs.entity.Screening;
import com.hotdog.ctbs.entity.Movie;
import com.hotdog.ctbs.entity.CinemaRoom;
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


    @Test
    void createMethod(){

        System.out.println("Testing createScreening method");
        // date to be 2025-05-01
        screeningImpl.createScreening("Spider-Man", "morning",
                true, LocalDate.of(2025, 5, 1), 2);
        // create another different
        screeningImpl.createScreening("Spider-Man", "evening",
                true, LocalDate.of(2025, 5, 1), 2);

        screeningImpl.createScreening("Inception", "afternoon",
                true, LocalDate.of(2025, 5, 5), 4);

        screeningImpl.createScreening("Inception", "evening",
                true, LocalDate.of(2025, 5, 30), 5);



    }

    @Test
    void getter(){

        // get a list of screenings object in string
        System.out.println("Testing getAllScreenings method");
        System.out.println(screeningImpl.getAllScreenings());
        System.out.println();

        // test getAllScreeningsByMovieTitle(String movieTitle)
        System.out.println("Testing getAllScreeningsByMovieTitle method");
        System.out.println(screeningImpl.getAllScreeningsByMovieTitle("Spider-Man"));
        System.out.println();




    }


}

