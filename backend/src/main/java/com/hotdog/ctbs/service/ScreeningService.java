package com.hotdog.ctbs.service;

import com.hotdog.ctbs.entity.CinemaRoom;
import com.hotdog.ctbs.entity.Movie;
import com.hotdog.ctbs.entity.Screening;
import com.hotdog.ctbs.repository.CinemaRoomRepository;
import com.hotdog.ctbs.repository.MovieRepository;
import com.hotdog.ctbs.repository.ScreeningRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ScreeningService {

        String createScreening(String movieTitle, String showTime, Boolean isActive, LocalDate showDate, Integer cinemaRoomId);

        // get screening by id
        Screening getScreeningById(UUID id);

        // get method for manager
        // get all screenings (includes all inactive) for manager ***
        List<Screening> getAllScreenings();

        // get all screenings by movie title in date ascending order
        List<Screening> getAllScreeningsByMovieTitle(String movieTitle);


        // get all screenings by show time
        List<Screening> getAllScreeningsByShowTime(String showTime);

        // get all screenings by cinema room id
        List<Screening> getAllScreeningsByCinemaRoomId(Integer cinemaRoomId);


        // get specific screening by movie id, show time, show date, cinema room id
        Screening getScreeningByMovieTitleAndShowTimeAndShowDateAndCinemaRoomId(String movieTitle, String showTime, LocalDate showDate, Integer cinemaRoomId);

        // get screening method for customers
        // get all active screenings
        List<Screening> getAllActiveScreenings();

        // get all active screening by movie title in show date ascending order
        List<Screening> getAllActiveScreeningsByMovieTitle(String movieTitle);

        // 3. Update screening
        String updateScreening(String movieTitle,
                                      String showTime,
                                      LocalDate showDate,
                                      CinemaRoom cinemaRoom);

        // 4. Delete screening (in progress need to check if it is linked to any ticket)
        String deleteScreening(String movieTitle,
                                      String showTime,
                                      LocalDate showDate,
                                      CinemaRoom cinemaRoom);

        List<Screening> getAllScreeningsByShowDate(LocalDate showDate);


}
