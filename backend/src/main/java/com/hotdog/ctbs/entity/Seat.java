package com.hotdog.ctbs.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "seat")
public class Seat {
    private UUID id;

    private CinemaRoom cinemaRoom;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "uuid", nullable = false)
    public UUID getId() {
        return id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_room")
    public CinemaRoom getCinemaRoom() {
        return cinemaRoom;
    }

    //TODO [JPA Buddy] generate columns from DB
}