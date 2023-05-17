package com.hotdog.ctbs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
    protected UUID id;

    @Column(name = "is_active", nullable = false)
    protected boolean isActive;

    @Column(name = "title", nullable = false, length = Integer.MAX_VALUE)
    protected String title;

    @Column(name = "genre", nullable = false, length = Integer.MAX_VALUE)
    protected String genre;

    @Column(name = "description", nullable = false, length = Integer.MAX_VALUE)
    protected String description;

    @Column(name = "release_date", nullable = false)
    protected LocalDate releaseDate;

    @Column(name = "image_url", length = Integer.MAX_VALUE)
    protected String imageUrl;

    @Column(name = "landscape_image_url", length = Integer.MAX_VALUE)
    protected String landscapeImageUrl;

    @Column(name = "content_rating", nullable = false, length = Integer.MAX_VALUE)
    protected String contentRating;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    protected Set<Screening> screenings = new LinkedHashSet<>();

    @SneakyThrows
    @Override
    public String toString()
    {
        ObjectNode json = new ObjectMapper().createObjectNode();
        json.put("title", title);
        json.put("genre", genre);
        json.put("description", description);
        json.put("releaseDate", releaseDate.toString());
        json.put("imageUrl", imageUrl);
        json.put("landscapeImageUrl", landscapeImageUrl);
        json.put("isActive", isActive);
        json.put("contentRating", contentRating);
        return json.toString();
    }

    // Method to compare two objects
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Movie that)) return false;
        return id.equals(that.id) &&
               title.equalsIgnoreCase(that.title);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, title);
    }
}