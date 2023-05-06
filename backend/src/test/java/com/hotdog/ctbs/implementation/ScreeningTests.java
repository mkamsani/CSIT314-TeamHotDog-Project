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
        screeningImpl.createScreening("Spider-Man", "morning", true, LocalDate.of(2025, 5, 1), 2);

    }

    /// ERROR (LAZY INITIALISER) - NEED TO FIX
        /*System.out.println("Testing getAllScreenings method");
        System.out.println(screeningImpl.getAllScreenings());
        System.out.println();*/


    @Test
    void getter(){
        // /// ERROR (LAZY INITIALISER) - NEED TO FIX
        //        /*System.out.println("Testing getAllScreenings method");
        //        System.out.println(screeningImpl.getAllScreenings());
        //        System.out.println();*/

        // test public Screening getScreeningByMovieIdAndShowTimeAndShowDateAndCinemaRoomId(UUID movieId, String showTime, LocalDate showDate, Integer cinemaRoomId)
        //    {
        //        return screeningRepo.findScreeningByMovieIdAndShowTimeAndShowDateAndCinemaRoomId(movieId, showTime, showDate, cinemaRoomId);
        //    }

        // test getScreeningByMovieIdAndShowTimeAndShowDateAndCinemaRoomId(movieId, showTime, showDate, cinemaRoomId);
        System.out.println("Testing getScreeningByMovieIdAndShowTimeAndShowDateAndCinemaRoomId method");
        screeningImpl.getScreeningByMovieTitleAndShowTimeAndShowDateAndCinemaRoomId("Spider-Man", "morning", LocalDate.of(2025, 5, 1), 2);
        System.out.println();

        // get a list of screenings object in string
        System.out.println("Testing getAllScreenings method");
        System.out.println(screeningImpl.getAllScreenings());

        System.out.println();


        // i want to return a screening object in a string format using this method









    }


}

