package com.hotdog.ctbs.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Builder
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
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_room")
    private CinemaRoom cinemaRoom;

    @Column(name = "seat_row", nullable = false, length = 1)
    private char seatRow;

    @Column(name = "seat_column", nullable = false)
    private Integer seatColumn;

    @OneToMany(mappedBy = "seat")
    private Set<Ticket> tickets = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Seat that)) return false;
        return id.equals(that.id)
               && cinemaRoom.equals(that.cinemaRoom)
               && seatRow == that.seatRow
               && seatColumn.equals(that.seatColumn);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, cinemaRoom, seatRow, seatColumn);
    }

    @Override
    public String toString()
    {
        ObjectNode json = new ObjectMapper().createObjectNode();
        json.put("row",      String.valueOf(seatRow));
        json.put("column",   String.valueOf(seatColumn));
        json.put("room",     cinemaRoom.getId().toString());
        json.put("seatCode", cinemaRoom.getId().toString() + seatRow + seatColumn);
        return json.toString();
    }
}