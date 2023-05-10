package com.hotdog.ctbs.service;

import com.hotdog.ctbs.entity.Seat;
import jakarta.transaction.Transactional;

import java.util.UUID;
import java.util.List;

public interface SeatService {

    // get a seat by id
    Seat getSeatById(UUID id);

    @Transactional
    Seat getSeatByRowAndColumnAndCinemaRoomId(char row, Integer column, Integer cinemaRoomId);
}
