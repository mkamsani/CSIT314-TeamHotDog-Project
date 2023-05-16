package com.hotdog.ctbs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
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
    private Integer id;

    @Column(name = "is_active", nullable = false)
    @JsonIgnore
    private Boolean isActive;

    @OneToMany(mappedBy = "cinemaRoom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Seat> seats = new LinkedHashSet<>();

    @SneakyThrows
    @Override
    public String toString()
    {
        ObjectNode json = new ObjectMapper().createObjectNode();
        json.put("id",          id.toString());
        json.put("isActive",    isActive.toString());
        json.put("capacity",    seats.size()); // Number of seats linked cinema room.
        return json.toString();
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
