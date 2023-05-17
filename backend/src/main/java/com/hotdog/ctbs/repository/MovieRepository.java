package com.hotdog.ctbs.repository;

import com.hotdog.ctbs.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovieRepository extends JpaRepository<Movie, UUID> {
    Optional<Movie> findMovieByTitle(final String title);

    Optional<Movie> findMovieByTitleAndIsActiveTrue(final String title);

    default List<String> findAllTitles()
    {
        return findAll().stream().map(Movie::getTitle).toList();
    }

    default List<String> findAllContentRatings()
    {
        return findAll().stream().map(Movie::getContentRating).toList();
    }
}
