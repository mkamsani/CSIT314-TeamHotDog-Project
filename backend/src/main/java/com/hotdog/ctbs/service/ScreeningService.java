package com.hotdog.ctbs.service;

import com.hotdog.ctbs.entity.Screening;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScreeningService {

        void createScreening(String movieTitle, String showTime, LocalDate showDate, Integer cinemaRoomId);
        Screening getScreeningById(UUID id);

        List<Screening> getAllScreenings();

        // get all screenings by movie title follow the sort order
        List<Screening> getAllScreeningsByMovieTitle(String movieTitle);



        // get all screenings by show date follow the sort order
        List<Screening> getAllScreeningsByShowDate(LocalDate showDate);


        // get all screenings by show time follow the sort order
        List<Screening> getAllScreeningsByShowTime(String showTime);


        // get all screenings by cinema room id follow the sort order
        List<Screening> getAllScreeningsByCinemaRoomId(Integer cinemaRoomId);



        // get specific screening by movie id, show time, show date, cinema room id
        Screening getScreeningByMovieTitleAndShowTimeAndShowDateAndCinemaRoomId(String movieTitle, String showTime, LocalDate showDate, Integer cinemaRoomId);

        // get all active screening method for customers usage
        // (customer does not need to see inactive screenings)
        /*List<Screening> getAllActiveScreenings();

        // get all active screenings by movie title
        List<Screening> getAllActiveScreeningsByMovieTitle(String movieTitle);*/


        // get all active screening method for customers usage
        // (customer does not need to see inactive screenings)
        // ** customer should only able to see active screenings
        // 1st order ==> show date in ascending order
        // 2nd order ==> show time in ascending order
        // 3rd order ==> cinema room in ascending order
        // 4th order ==> movie in ascending order
        @Transactional
        List<Screening> getAllActiveScreenings();

        // get all active screenings by movie title (condition getStatus == "active")
        // (customer does not need to see inactive screenings)
        @Transactional
        List<Screening> getAllActiveScreeningsByMovieTitle(String movieTitle);

        // 3. Update screening
        // *** can update all attribute of a screening except the "isActive" (status)
        // update a screening require all 4 fields (movieTitle, showTime, showDate, cinemaRoomId)
        // if a screening is inactive, cant update
        void updateScreening(String currentMovieTitle, String currentShowTime,
                             LocalDate currentShowDate, Integer currentCinemaRoomId,
                             String newMovieTitle, String newShowTime, LocalDate newShowDate, Integer newCinemaRoomId);


    // suspend screening method (not used)
    @Transactional
    void suspendScreening(String movieTitle,
                          String currentShowTime,
                          LocalDate currentShowDate,
                          Integer cinemaRoomId);
}
