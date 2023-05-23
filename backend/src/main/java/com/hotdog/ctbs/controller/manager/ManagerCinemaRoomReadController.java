package com.hotdog.ctbs.controller.manager;

import com.hotdog.ctbs.entity.CinemaRoom;
import com.hotdog.ctbs.repository.CinemaRoomRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/manager/cinemaRoom")
public class ManagerCinemaRoomReadController {

    private final CinemaRoom cinemaRoom;

    private final CinemaRoomRepository cinemaRoomRepository;

    public ManagerCinemaRoomReadController(CinemaRoomRepository cinemaRoomRepository)
    {
        this.cinemaRoomRepository = cinemaRoomRepository;
        this.cinemaRoom = new CinemaRoom();
    }

    /**
     * Read a JSON array of {@code CinemaRoom} object(s)
     */

    @GetMapping("/read/{param}")
    public ResponseEntity<String> Read(@PathVariable final String param)
    {
        try {
            // return json string from readCinemaRoom method
            String json = cinemaRoom.readCinemaRoom(cinemaRoomRepository, param);
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
