package com.hotdog.ctbs.repository;

import com.hotdog.ctbs.entity.CinemaRoom;
import com.hotdog.ctbs.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SeatRepository extends JpaRepository<Seat, UUID> {
    Optional<Seat> findSeatBySeatRowAndSeatColumnAndCinemaRoom(char seatRow,
                                                               Integer seatColumn,
                                                               CinemaRoom cinemaRoom);
}
