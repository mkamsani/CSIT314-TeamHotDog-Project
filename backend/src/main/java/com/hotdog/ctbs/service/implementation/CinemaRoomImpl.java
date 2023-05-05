package com.hotdog.ctbs.service.implementation;


import com.hotdog.ctbs.entity.CinemaRoom;
import com.hotdog.ctbs.repository.CinemaRoomRepository;
import org.springframework.stereotype.Service;
import com.hotdog.ctbs.service.CinemaRoomService;

import java.util.List;

@Service
public class CinemaRoomImpl implements CinemaRoomService{

    final CinemaRoomRepository cinemaRoomRepository;

    public CinemaRoomImpl(CinemaRoomRepository cinemaRoomRepository) {
        this.cinemaRoomRepository = cinemaRoomRepository;
    }

    // return a list of all cinema room ids
    @Override
    public List<Integer> getAllCinemaRoomIds(){
        return cinemaRoomRepository.findAll().stream()
                .map(CinemaRoom::getId)
                .toList();
    }


    // return a list of all cinema room
    @Override
    public List<CinemaRoom> getAllCinemaRoom(){
        return cinemaRoomRepository.findAll();
    }


    @Override
    public void checkCinemaRoomExists(Integer ID){
        if (cinemaRoomRepository.findCinemaRoomById(ID) == null){
            throw new IllegalStateException("Cinema Room with ID " + ID + " does not exist");
        }
    }

    // update cinema room status
    @Override
    public void updateCinemaRoom(Integer currentId, Boolean isActive){
        checkCinemaRoomExists(currentId);
        CinemaRoom cinemaRoom = cinemaRoomRepository.findCinemaRoomById(currentId);
        cinemaRoom.setIsActive(isActive);
        cinemaRoomRepository.save(cinemaRoom);
    }

    // return a list of all CinemaRooms that are active
    @Override
    public List<CinemaRoom> getAllActiveCinemaRooms(){
        return cinemaRoomRepository.findAll().stream()
                .filter(CinemaRoom::getIsActive)
                .toList();
    }



    /*
    // cannot delete cinema room because it linked to all seat
    // delete cinema room
    @Override
    public void deleteCinemaRoom(Integer id){
        if (cinemaRoomRepository.findCinemaRoomById(id) == null){
            throw new IllegalStateException("Cinema Room with ID " + id + " does not exist");
        }
        cinemaRoomRepository.deleteById(id);
    }

     */
}
