package com.hotdog.ctbs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotdog.ctbs.repository.CinemaRoomRepository;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Builder
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

    @OneToMany(mappedBy = "cinemaRoom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    protected Set<Seat> seats = new LinkedHashSet<>();

    @Transient
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public String readCinemaRoom(CinemaRoomRepository cinemaRoomRepository, final String param) {
        List<CinemaRoom> cinemaRooms = null;
        if (param.matches("\\d+")){
            cinemaRooms = List.of(cinemaRoomRepository.findById(Integer.parseInt(param)).orElse(null));
        }
        else
        {
            cinemaRooms = cinemaRoomRepository.findAll();
        }

        ArrayNode arrayNode = objectMapper.createArrayNode();
        for(CinemaRoom cinemaRoom : cinemaRooms) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("id", cinemaRoom.id);
            objectNode.put("isActive", cinemaRoom.isActive);
            objectNode.put("capacity", cinemaRoom.seats.size());
            arrayNode.add(objectNode);
        }

        return arrayNode.toString();
    }
}
