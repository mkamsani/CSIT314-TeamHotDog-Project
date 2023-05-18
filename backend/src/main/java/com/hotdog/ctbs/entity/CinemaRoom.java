package com.hotdog.ctbs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotdog.ctbs.repository.CinemaRoomRepository;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "cinema_room")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CinemaRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    protected Integer id;

    @Column(name = "is_active", nullable = false)
    @JsonIgnore
    protected Boolean isActive;

    @OneToMany(mappedBy = "cinemaRoom")
    @JsonIgnore
    protected Set<Seat> seats = new LinkedHashSet<>();

    @Transient
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    //////////////////////////////// Service /////////////////////////////////

    public String readCinemaRoom(CinemaRoomRepository cinemaRoomRepository, final String param)
    {
        List<CinemaRoom> cinemaRooms;

        if (param.matches("\\d+")) {
            int id = Integer.parseInt(param);
            CinemaRoom cinemaRoom = cinemaRoomRepository.findById(id).orElse(null);
            if (cinemaRoom == null) throw new IllegalArgumentException("Cinema room with does not exist: " + param);
            cinemaRooms = List.of(cinemaRoom);
        } else if (param.equals("all")) {
            cinemaRooms = cinemaRoomRepository.findAll();
        } else {
            throw new IllegalArgumentException("Invalid parameter: " + param);
        }

        ArrayNode an = objectMapper.createArrayNode();
        for (CinemaRoom cinemaRoom : cinemaRooms) {
            ObjectNode on = objectMapper.createObjectNode();
            on.put("id", cinemaRoom.id);
            on.put("isActive", cinemaRoom.isActive.toString());
            on.put("capacity", cinemaRoom.seats.size());
            an.add(on);
        }
        return an.toString();
    }
}
