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
import java.util.List;
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
                LocalDate.of(2025, 5, 1), 1);

    }

    @Test
    void getter() {

            // test List<Screening> getAllScreenings();
            System.out.println("Testing getAllScreenings method");
            System.out.println(screeningImpl.getAllScreenings());
            System.out.println();

            // test List<Screening> getAllScreeningsByMovieTitle(String movieTitle);
            System.out.println("Testing getAllScreeningsByMovieTitle method");
            System.out.println(screeningImpl.getAllScreeningsByMovieTitle("Spider-Man"));
            System.out.println();

            // test List<Screening> getAllScreeningsByShowDate(LocalDate showDate);
            System.out.println("Testing getAllScreeningsByShowDate method");
            System.out.println(screeningImpl.getAllScreeningsByShowDate(LocalDate.of(2023, 5, 23)));
            System.out.println();

            // test List<Screening> getAllScreeningsByShowTime(String showTime);
            System.out.println("Testing getAllScreeningsByShowTime method");
            System.out.println(screeningImpl.getAllScreeningsByShowTime("morning"));
            System.out.println();

            // test List<Screening> getAllScreeningsByCinemaRoomId(Integer cinemaRoomId);
            System.out.println("Testing getAllScreeningsByCinemaRoomId method");
            System.out.println(screeningImpl.getAllScreeningsByCinemaRoomId(1));
            System.out.println();

            // test Screening getScreeningByMovieTitleAndShowTimeAndShowDateAndCinemaRoomId(String movieTitle, String showTime, LocalDate showDate, Integer cinemaRoomId);
            System.out.println("Testing getScreeningByMovieTitleAndShowTimeAndShowDateAndCinemaRoomId method");
            System.out.println(screeningImpl.getScreeningByShowTimeAndShowDateAndCinemaRoomId("morning", LocalDate.of(2023, 5, 23), 1));
            System.out.println();

            // test List<Screening> getAllActiveScreenings();
            System.out.println("Testing getAllActiveScreenings method");
            System.out.println(screeningImpl.getAllActiveScreenings());
            System.out.println();

            System.out.println("Testing getAllActiveScreeningsByMovieTitle method");
            System.out.println(screeningImpl.getAllActiveScreeningsByMovieTitle("Spider-Man"));
            System.out.println();

    }

    @Test
    void updateMethod() {

            System.out.println("Testing updateScreening method");
            screeningImpl.updateScreening("afternoon", LocalDate.of(2023, 5, 23), 1,
                    "Avatar", "afternoon", LocalDate.of(2055, 5, 5), 5
            );
            System.out.println(screeningImpl.getAllScreenings());
            System.out.println();


    }

    @Test
    void suspendMethod() {

        System.out.println("Testing suspendScreening method");
        screeningImpl.suspendScreening("morning", LocalDate.of(2023, 5, 23), 1);

        System.out.println(screeningImpl.getAllScreenings());
        System.out.println();
        }

    @Test
    void violationMehthod()
    {
        // Attempt to violate the createScreening method
        // Why surround in a try-catch block?
        // Because the violation is expected, and it should not fail the test in a CI/CD pipeline.
        try {
            screeningImpl.createScreening("Ultraman", "midnight",
                    LocalDate.of(2025, 5, 1), 1);
        } catch (Exception e) {

            System.out.println("Expected exception: " + e.getMessage());
        }

    }

    @Test
    void cancelMethod()
    {
        screeningImpl.cancelScreening( "morning", LocalDate.of(2023, 5, 23), 1);
    }


}

