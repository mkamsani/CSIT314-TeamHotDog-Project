package com.hotdog.ctbs.repository;

import com.hotdog.ctbs.entity.CinemaRoom;
import com.hotdog.ctbs.entity.Movie;
import com.hotdog.ctbs.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScreeningRepository extends JpaRepository<Screening, UUID> {

    Screening findScreeningById(UUID id);

    Optional<List<Screening>> findScreeningsByMovieTitle(String movieTitle);

    Optional<List<Screening>> findScreeningsByShowTime(String showTime);

    Optional<List<Screening>> findScreeningsByShowDate(LocalDate showDate);

    Optional<List<Screening>> findScreeningsByCinemaRoom(CinemaRoom cinemaRoom);

    Optional<List<Screening>> findScreeningsByIsActive(Boolean isActive);

    Optional<List<Screening>> findScreeningsByMovieTitleAndIsActive(String movieTitle, boolean b);

    Optional<List<Screening>> findScreeningsByShowDateAndShowTime(LocalDate showDate, String showTime);

    Optional<List<Screening>> findScreeningsByCinemaRoomIdAndShowDate(Integer cinemaRoomId, LocalDate showDate);

    Screening findScreeningByMovieTitleAndShowTimeAndShowDateAndCinemaRoomId(String movieTitle, String showTime, LocalDate showDate, Integer cinemaRoomId);

    // need to find all active later than or equal to now(localdatetime)
    Optional <List<Screening>> findByIsActiveAndShowDateGreaterThanEqual(Boolean isActive, LocalDate showDate);


    Optional<List<Screening>> findScreeningsByMovieTitleAndIsActiveAndShowDateGreaterThanEqual(String movieTitle, boolean b, LocalDate now);
}
