package com.hotdog.ctbs.repository;

import com.hotdog.ctbs.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovieRepository extends JpaRepository<Movie, UUID> {
    Optional<Movie> findMovieByTitle(final String title);

    Optional<Movie> findMovieByTitleAndIsActiveTrue(final String title);

    /** @return Movies bookable by customer. */
    @Query("""
            SELECT movie
            FROM Movie movie
            INNER JOIN Screening screening ON screening.movie.id = movie.id
            WHERE movie.isActive = TRUE
              AND screening.showDate >= NOW()
              AND screening.status = 'active'
            """)
    List<Movie> findCustomerMovies();

    default List<String> findAllTitles()
    {
        return findAll().stream().map(Movie::getTitle).toList();
    }

    default List<String> findAllContentRatings()
    {
        return findAll().stream().map(Movie::getContentRating).toList();
    }
}
