package com.hotdog.ctbs.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "cinema_room")
public class CinemaRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @SneakyThrows
    @Override
    public String toString()
    {
        return new ObjectMapper().registerModule(new JavaTimeModule())
                .writeValueAsString(this);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof CinemaRoom that)) return false;
        return id.equals(that.id) && isActive.equals(that.isActive);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, isActive);
    }

}
