package com.hotdog.ctbs.service.implementation;

import com.hotdog.ctbs.entity.CinemaRoom;
import com.hotdog.ctbs.repository.CinemaRoomRepository;
import com.hotdog.ctbs.service.CinemaRoomService;

import java.util.List;

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

    // return a list of all cinema room capacities
    @Override
    public List<Integer> getAllCinemaRoomCapacities(){
        return cinemaRoomRepository.findAll().stream()
                .map(CinemaRoom::getCapacity)
                .toList();
    }

    @Override
    public void checkCinemaRoomExists(Integer ID){
        if (cinemaRoomRepository.findCinemaRoomById(ID) == null){
            throw new IllegalStateException("Cinema Room with ID " + ID + " does not exist");
        }
    }

    // update cinema room capacity
    @Override
    public void updateCinemaRoomCapacity(Integer id, Integer capacity){
        checkCinemaRoomExists(id);
        CinemaRoom cinemaRoom = cinemaRoomRepository.findCinemaRoomById(id);
        cinemaRoom.setCapacity(capacity);
        cinemaRoomRepository.save(cinemaRoom);
    }

    // delete cinema room
    @Override
    public void deleteCinemaRoom(Integer id){
        if (cinemaRoomRepository.findCinemaRoomById(id) != null){
            throw new IllegalStateException("Cinema Room with ID " + id + " does not exist");
        }
        cinemaRoomRepository.deleteById(id);
    }
}
