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

    Optional<List<Screening>> findScreeningsByMovie(Movie movie);

    Optional<List<Screening>> findScreeningsByShowDate(LocalDate showDate);

    Optional<List<Screening>> findScreeningByShowTime(String showTime);

    Optional<List<Screening>> findScreeningsByIsActive(Boolean isActive);

    Optional<List<Screening>> findScreeningsByShowDateAndShowTime(LocalDate showDate, String showTime);

    Screening findScreeningByMovieIdAndShowTimeAndShowDateAndCinemaRoomId(UUID movieId, String showTime, LocalDate showDate, Integer cinemaRoomId);

    Optional<List<Screening>> findScreeningsByCinemaRoom(CinemaRoom cinemaRoom);

    Optional<List<Screening>> findScreeningsByMovieAndIsActive(Movie movie, Boolean isActive);

    Optional<List<Screening>> findScreeningsByMovieAndIsActiveOrderByShowDateAsc(Movie movie, boolean b);

    Optional<List<Screening>> findScreeningsByMovieOrderByShowDateAsc(Movie movie);

    Optional<List<Screening>> findScreeningsByCinemaRoomIdAndShowDate(Integer cinemaRoomId, LocalDate showDate);

    List<Screening> findAllByOrderByShowDateAsc();

    List<Screening> findAllByOrderByShowDateAscShowTimeAsc();

    List<Screening> findAllByOrderByShowDateAscShowTimeAscCinemaRoomAscMovieAsc();

    Optional<List<Screening>> findScreeningsByMovieTitleOrderByShowDateAscShowTimeAscCinemaRoomAsc(String movieTitle);

    Optional<List<Screening>> findScreeningsByShowDateOrderByShowTimeAscCinemaRoomAscMovieAsc(LocalDate showDate);

    Optional<List<Screening>> findScreeningsByShowDateOrderByShowDateAscShowTimeAscCinemaRoomAscMovieAsc(LocalDate showDate);

    Optional<List<Screening>> findScreeningsByShowTimeOrderByShowDateAscShowTimeAscCinemaRoomAscMovieAsc(String showTime);

    Optional<List<Screening>> findScreeningsByMovieTitleOrderByShowDateAscShowTimeAscCinemaRoomAscMovieAsc(String movieTitle);

    Optional<List<Screening>> findScreeningsByCinemaRoomIdOrderByShowDateAscShowTimeAscCinemaRoomAscMovieAsc(Integer cinemaRoomId);

    Screening findScreeningByMovieTitleAndShowTimeAndShowDateAndCinemaRoomId(String movieTitle, String showTime, LocalDate showDate, Integer cinemaRoomId);

    Optional<List<Screening>> findScreeningsByIsActiveOrderByShowDateAscShowTimeAscCinemaRoomAscMovieAsc(boolean b);

    Optional<List<Screening>> findScreeningsByMovieTitleAndIsActiveOrderByShowDateAscShowTimeAscCinemaRoomAscMovieAsc(String movieTitle, boolean b);
}
