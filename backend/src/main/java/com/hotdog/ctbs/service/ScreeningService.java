package com.hotdog.ctbs.service;

import com.hotdog.ctbs.entity.CinemaRoom;
import com.hotdog.ctbs.entity.Movie;
import com.hotdog.ctbs.entity.Screening;

import java.time.LocalDate;
import java.util.List;

public interface ScreeningService {

    // get all screenings
    List<Screening> getAllScreenings();

    List<Screening> getScreeningsByDate(LocalDate localDate);

    String createScreening(String movieTitle, String showTime, Boolean isActive, LocalDate showDate, Integer cinemaRoomId);


    String updateScreening(Movie movie, String showTime, Boolean isActive, LocalDate showDate,
                           CinemaRoom cinemaRoom);
}
