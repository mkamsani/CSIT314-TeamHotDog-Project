package com.hotdog.ctbs.entity;

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
@Table(name = "movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false, length = Integer.MAX_VALUE)
    private String title;

    @Column(name = "genre", nullable = false, length = Integer.MAX_VALUE)
    private String genre;

    @Column(name = "description", nullable = false, length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @Column(name = "image_url", length = Integer.MAX_VALUE)
    private String imageUrl;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "content_rating", nullable = false, length = Integer.MAX_VALUE)
    private String contentRating;

    @SneakyThrows
    @Override
    public String toString()
    {
        return new ObjectMapper().registerModule(new JavaTimeModule())
                .writeValueAsString(this);
    }

    // Method to compare two objects
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Movie that)) return false;
        return id.equals(that.id) && title.equals(that.title);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, title);
    }
}