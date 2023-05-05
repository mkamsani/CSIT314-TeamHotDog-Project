package com.hotdog.ctbs.service;

import com.hotdog.ctbs.entity.CinemaRoom;
import java.util.List;


public interface CinemaRoomService {
    List<Integer> getAllCinemaRoomIds();

    // return a list of all cinema room
    List<CinemaRoom> getAllCinemaRoom();

    // check if cinema room exists
    void checkCinemaRoomExists(Integer ID);

    // update cinema room status
    void updateCinemaRoom(Integer currentId, Boolean isActive);

    // cannot delete cinema room
    //void deleteCinemaRoom(Integer id);
}
