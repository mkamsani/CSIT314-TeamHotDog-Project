package com.hotdog.ctbs.repository;

import com.hotdog.ctbs.entity.CinemaRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CinemaRoomRepository extends JpaRepository<CinemaRoom, Integer> {

    CinemaRoom findCinemaRoomById(Integer id);

}
