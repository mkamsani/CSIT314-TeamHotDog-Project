package com.hotdog.ctbs.entity;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;
import java.util.Objects;

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
    private UUID id;

    /*@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;*/

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Column(name = "show_time", nullable = false)
    private String showTime; // ONLY 'morning', 'afternoon', 'evening', 'midnight'

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "show_date", nullable = false)
    private LocalDate showDate;

    /*@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cinema_room", nullable = false)
    private CinemaRoom cinemaRoom;*/

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cinema_room", nullable = false)
    private CinemaRoom cinemaRoom;

    @SneakyThrows
    @Override
    public String toString()
    {
        ObjectNode json = new ObjectMapper().createObjectNode();
        json.put("movie", movie.getTitle());
        json.put("showTime",   showTime);
        json.put("isActive",   isActive);
        json.put("showDate",   showDate.toString());
        json.put("cinemaRoom", cinemaRoom.getId());
        return json.toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Screening that)) return false;
        return id.equals(that.id) && movie.equals(that.movie) && showTime.equals(that.showTime) && cinemaRoom.equals(that.cinemaRoom) && isActive.equals(that.isActive) && showDate.equals(that.showDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, movie, showTime, cinemaRoom, isActive, showDate);
    }
}