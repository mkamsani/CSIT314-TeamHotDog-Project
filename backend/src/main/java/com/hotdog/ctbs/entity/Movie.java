package com.hotdog.ctbs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "movie")
@JsonIgnoreProperties({"screenings"})
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", nullable = false)
    private UUID id;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

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

    @Column(name = "landscape_image_url", length = Integer.MAX_VALUE)
    private String landscapeImageUrl;

    @Column(name = "content_rating", nullable = false, length = Integer.MAX_VALUE)
    private String contentRating;

    /*@OneToMany(mappedBy = "movie")
    private Set<Screening> screenings = new LinkedHashSet<>();*/

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Screening> screenings = new LinkedHashSet<>();

    @SneakyThrows
    @Override
    public String toString()
    {

        ObjectNode json = new ObjectMapper().createObjectNode();
        //json.put("id",          id.toString());
        json.put("title",         title);
        json.put("genre",         genre);
        json.put("description",   description);
        json.put("releaseDate",   releaseDate.toString());
        json.put("imageUrl",      imageUrl);
        json.put("landscapeImageUrl", landscapeImageUrl);
        json.put("isActive",      isActive);
        json.put("contentRating", contentRating);
        return json.toString();
    }

    // Method to compare two objects
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Movie that)) return false;
        return id.equals(that.id) && title.equalsIgnoreCase(that.title);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, title);
    }

    public int compareTo(Movie movie) {
        return this.getId().compareTo(movie.getId());
    }
}