package com.hotdog.ctbs.implementation;

import com.hotdog.ctbs.entity.Seat;
import com.hotdog.ctbs.entity.CinemaRoom;
import com.hotdog.ctbs.repository.CinemaRoomRepository;
import com.hotdog.ctbs.repository.SeatRepository;
import com.hotdog.ctbs.service.SeatService;

import com.hotdog.ctbs.service.implementation.ScreeningImpl;
import com.hotdog.ctbs.service.implementation.SeatImpl;
import com.hotdog.ctbs.service.implementation.CinemaRoomImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class SeatTests {

    @Autowired
    private SeatImpl seatImpl;


    @Test
    void getter(){

        // test getSeatByRowAndColumnAndCinemaRoomId
        System.out.println("Test getSeatByRowAndColumnAndCinemaRoomId");
        System.out.println(seatImpl.getSeatByRowAndColumnAndCinemaRoomId('A', 1, 1));
        System.out.println();

        // print a seat detail
        System.out.println("Test getSeatById");
        System.out.println(seatImpl.getSeatById(UUID.fromString("a0b9b4e0-0b1a-4b1a-9b0a-0b1a0b1a0b1a")));
        System.out.println();

    }
}
