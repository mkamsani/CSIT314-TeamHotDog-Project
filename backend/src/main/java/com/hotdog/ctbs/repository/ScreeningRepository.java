package com.hotdog.ctbs.repository;

import com.hotdog.ctbs.entity.Movie;
import com.hotdog.ctbs.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScreeningRepository extends JpaRepository<Screening, UUID> {

    Screening findScreeningById(UUID id);

    Optional<List<Screening>> findScreeningsByMovieTitle(String movieTitle);

    /** Check if a {@code CinemaRoom} object is a duplicate. */
    Boolean existsByShowTimeAndAndShowDateAndAndCinemaRoom(String showTime, LocalDate showDate, Integer cinemaRoomId);

    Optional <Screening> findScreeningByShowTimeAndAndShowDateAndAndCinemaRoom(String showTime, LocalDate showDate, Integer cinemaRoomId);
    List<Screening> findScreeningsByShowDateAndCinemaRoom(LocalDate showDate, Integer cinemaRoomId);

    @Query("SELECT s FROM Screening s WHERE s.status = 'active' AND s.showDate >= :today")
    List<Screening> findActiveScreeningsLaterOrEqual(@Param("today") LocalDate today);

    @Query("SELECT s FROM Screening s WHERE s.status = 'active' AND s.showDate >= :today AND (:movie IS NULL OR s.movie = :movie)")
    List<Screening> findActiveScreeningsForMovieAndLaterOrEqual(@Param("today") LocalDate today, @Param("movie") Movie movie);

}
