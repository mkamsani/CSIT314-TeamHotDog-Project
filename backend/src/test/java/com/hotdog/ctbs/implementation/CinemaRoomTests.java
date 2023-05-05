package com.hotdog.ctbs.implementation;

import com.hotdog.ctbs.entity.CinemaRoom;
import com.hotdog.ctbs.service.implementation.CinemaRoomImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest
public class CinemaRoomTests {

    @Autowired
    private CinemaRoomImpl cinemaRoomImpl;

    @Test
    void allMethod(){

        // test List<Integer> getAllCinemaRoomIds();
        System.out.println("Test List<Integer> getAllCinemaRoomIds()");
        System.out.println(cinemaRoomImpl.getAllCinemaRoomIds());
        System.out.println();

        // test List<CinemaRoom> getAllCinemaRoom();
        System.out.println("Test List<CinemaRoom> getAllCinemaRoom()");
        System.out.println(cinemaRoomImpl.getAllCinemaRoom());
        System.out.println();

        // test void checkCinemaRoomExists(Integer ID);
        System.out.println("Test void checkCinemaRoomExists(Integer ID)");
        cinemaRoomImpl.checkCinemaRoomExists(1);
        System.out.println();

        // test void updateCinemaRoom(Integer id, Boolean isActive);
        System.out.println("Test void updateCinemaRoom(Integer id, Boolean isActive)");
        cinemaRoomImpl.updateCinemaRoom(3, false);
        System.out.println();
        // return a list of all cinema room after update
        System.out.println("Return a list of all cinema room after update");
        System.out.println(cinemaRoomImpl.getAllCinemaRoom());
        System.out.println();

        /*
        // cannot delete cinema room with id 1 because it is used in the database
        // test void deleteCinemaRoom(Integer id);
        System.out.println("Test void deleteCinemaRoom(Integer id)");
        cinemaRoomImpl.deleteCinemaRoom(1);
        System.out.println();

         */

    }




}

