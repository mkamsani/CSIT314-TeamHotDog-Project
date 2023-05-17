package com.hotdog.ctbs.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "seat")
@JsonIgnoreProperties({"tickets"})
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", nullable = false)
    protected UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_room")
    protected CinemaRoom cinemaRoom;

    @Column(name = "seat_row", nullable = false, length = 1)
    protected char seatRow;

    @Column(name = "seat_column", nullable = false)
    protected Integer seatColumn;

    @OneToMany(mappedBy = "seat")
    protected Set<Ticket> tickets = new LinkedHashSet<>();

    @Transient
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override // TODO : Remove
    public String toString()
    {
        ObjectNode json = objectMapper.createObjectNode();
        json.put("row", String.valueOf(seatRow));
        json.put("column", String.valueOf(seatColumn));
        json.put("room", cinemaRoom.getId().toString());
        json.put("seatCode", cinemaRoom.getId().toString() + seatRow + seatColumn);
        return json.toString();
    }
}