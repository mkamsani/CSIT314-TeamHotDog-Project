package com.hotdog.ctbs.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "screening")
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", nullable = false)
    protected UUID id;

    // @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    protected Movie movie;

    @Column(name = "show_time", nullable = false)
    protected String showTime; // ONLY 'morning', 'afternoon', 'evening', 'midnight'

    @Column(name = "status", nullable = false)
    protected String status; // ONLY 'active', 'suspended', 'cancelled'

    /*@Column(name = "is_active" , nullable = false)
    protected Boolean isActive;*/

    @Column(name = "show_date", nullable = false)
    protected LocalDate showDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cinema_room", nullable = false)
    protected CinemaRoom cinemaRoom;

    @SneakyThrows
    @Override
    public String toString()
    {
        ObjectNode json = new ObjectMapper().createObjectNode();
        json.put("movie", movie.getTitle());
        json.put("showTime", showTime);
        json.put("status", status);
        json.put("showDate", showDate.toString());
        json.put("cinemaRoom", cinemaRoom.getId());
        return json.toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Screening that)) return false;
        return id.equals(that.id) &&
               showTime.equals(that.showTime) &&
               cinemaRoom.equals(that.cinemaRoom) &&
               showDate.equals(that.showDate);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, showTime, cinemaRoom, showDate);
    }
}