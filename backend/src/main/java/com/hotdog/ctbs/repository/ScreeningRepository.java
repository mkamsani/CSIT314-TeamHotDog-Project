package com.hotdog.ctbs.repository;

import com.hotdog.ctbs.entity.CinemaRoom;
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

    Optional<List<Screening>> findScreeningsByMovieTitle(String movieTitle);

    Optional<Screening> findScreeningByShowTimeAndShowDateAndCinemaRoom_Id(String showTime,
                                                                           LocalDate showDate,
                                                                           Integer cinemaRoomId);

    List<Screening> findScreeningsByShowDateAndCinemaRoom(LocalDate showDate,
                                                          CinemaRoom cinemaRoom);

    @Query("SELECT s FROM Screening s WHERE s.status = 'active' AND s.showDate >= :today")
    List<Screening> findActiveScreeningsLaterOrEqual(@Param("today") LocalDate today);

    @Query("SELECT s FROM Screening s WHERE s.status = 'active' AND s.showDate >= :today AND (:movie IS NULL OR s.movie = :movie)")
    List<Screening> findActiveScreeningsForMovieAndLaterOrEqual(@Param("today") LocalDate today,
                                                                @Param("movie") Movie movie);
}
