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
        // first 4 screenings
        screeningImpl.createScreening("Spider-Man", "morning",
                LocalDate.of(2025, 5, 7), 1);
        // create another different
        screeningImpl.createScreening("Spider-Man", "afternoon",
                LocalDate.of(2025, 5, 14), 1);

        screeningImpl.createScreening("Inception", "evening",
                LocalDate.of(2025, 5, 22), 1);

        screeningImpl.createScreening("Inception", "midnight",
                LocalDate.of(2025, 5, 15), 1);


        // second 4 screenings
        screeningImpl.createScreening("Spider-Man", "morning",
                LocalDate.of(2025, 5, 1), 1);
        // create another different
        screeningImpl.createScreening("Spider-Man", "afternoon",
                LocalDate.of(2025, 5, 1), 1);

        screeningImpl.createScreening("Inception", "evening",
                LocalDate.of(2025, 5, 1), 1);

        screeningImpl.createScreening("Inception", "midnight",
                LocalDate.of(2025, 5, 1), 1);

        // third 4 screenings
        screeningImpl.createScreening("Spider-Man", "midnight",
                LocalDate.of(2025, 5, 1), 2);
        // create another different
        screeningImpl.createScreening("Spider-Man", "morning",
                LocalDate.of(2025, 5, 1), 2);

        screeningImpl.createScreening("Inception", "afternoon",
                LocalDate.of(2025, 5, 1), 2);

        screeningImpl.createScreening("Inception", "evening",
                LocalDate.of(2025, 5, 1), 2);

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
        System.out.println(screeningImpl.getAllScreeningsByShowDate(LocalDate.of(2023, 1, 1)));
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
        System.out.println(screeningImpl.getScreeningByMovieTitleAndShowTimeAndShowDateAndCinemaRoomId("Spider-Man", "morning", LocalDate.of(2023, 1, 1), 1));
        System.out.println();

        // test List<Screening> getAllActiveScreenings();
        System.out.println("Testing getAllActiveScreenings method");
        System.out.println(screeningImpl.getAllActiveScreenings());
        System.out.println();

        System.out.println("Testing getAllActiveScreeningsByMovieTitle method");
        System.out.println(screeningImpl.getAllActiveScreeningsByMovieTitle("Spider-Man"));
        System.out.println();

        try{
            // violate getAllActiveScreeningsByMovieTitle method
            System.out.println("Testing getAllActiveScreeningsByMovieTitle method");
            System.out.println(screeningImpl.getAllActiveScreeningsByMovieTitle("Batman Begins"));
            System.out.println();
        }
        catch (Exception e){
            System.out.println("Expected exception: " + e.getMessage());
        }


    }

    @Test
    void updateMethod() {
        System.out.println("Testing updateScreening method");
        screeningImpl.updateScreening(
                "Spider-Man", "afternoon", LocalDate.of(2023, 1, 1), 1,
                "Avatar", "afternoon", LocalDate.of(2025, 5, 1), 4
        );
        System.out.println(screeningImpl.getAllScreenings());
        System.out.println();
    }

    @Test
    void suspendMethod() {
        System.out.println("Testing suspendScreeningByIsActive method");
        screeningImpl.suspendScreeningByIsActive(
                "Spider-Man",
                "afternoon",
                LocalDate.of(2023, 1, 1), 1,
                false
        );
        System.out.println(screeningImpl.getAllScreenings());
        System.out.println();
        }
}

