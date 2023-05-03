package com.hotdog.ctbs.service;

import com.hotdog.ctbs.entity.CinemaRoom;
import java.util.List;

public interface CinemaRoomService {

    List<Integer> getAllCinemaRoomIds();

    List<Integer> getAllCinemaRoomCapacities();

    void checkCinemaRoomExists(Integer ID);

    void updateCinemaRoomCapacity(Integer id, Integer capacity);

    void deleteCinemaRoom(Integer id);
}
