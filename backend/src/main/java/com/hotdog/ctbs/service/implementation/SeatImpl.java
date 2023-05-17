package com.hotdog.ctbs.service.implementation;


import com.hotdog.ctbs.entity.Seat;
import com.hotdog.ctbs.entity.CinemaRoom;
import com.hotdog.ctbs.repository.*;
import com.hotdog.ctbs.service.SeatService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.List;

@Service
public class SeatImpl implements SeatService{

    final SeatRepository seatRepo;
    final CinemaRoomRepository cinemaRoomRepo;

    public SeatImpl(SeatRepository seatRepo, CinemaRoomRepository cinemaRoomRepo)
    {
        this.seatRepo = seatRepo;
        this.cinemaRoomRepo = cinemaRoomRepo;
    }

    // get a seat by id
    @Override
    public Seat getSeatById(UUID id){
        return seatRepo.findSeatById(id);
    }

    // get a seat by char row, integer column and cinema room id

    @Transactional
    @Override
    public Seat getSeatByRowAndColumnAndCinemaRoomId(char row, Integer column, Integer cinemaRoomId){

        CinemaRoom cinemaRoom = cinemaRoomRepo.findById(cinemaRoomId).orElse(null);
        if (cinemaRoom == null) {
            // TODO: throw exception
        }
        return seatRepo.findSeatBySeatRowAndAndSeatColumnAndCinemaRoom(row, column, cinemaRoom);

    }





}
