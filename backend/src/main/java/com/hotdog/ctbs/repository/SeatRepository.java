package com.hotdog.ctbs.repository;

import com.hotdog.ctbs.entity.CinemaRoom;
import com.hotdog.ctbs.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SeatRepository extends JpaRepository<Seat, UUID>{
    Optional<Seat> findSeatBySeatRowAndSeatColumnAndCinemaRoom(char seatRow, Integer seatColumn, CinemaRoom cinemaRoom);

    // might be used for the list of available seats
    List<Seat> findSeatsByCinemaRoom(CinemaRoom cinemaRoom);
}
