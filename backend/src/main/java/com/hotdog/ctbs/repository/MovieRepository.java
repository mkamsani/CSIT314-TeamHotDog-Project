package com.hotdog.ctbs.repository;

import com.hotdog.ctbs.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;


public interface MovieRepository extends JpaRepository<Movie, UUID> {

    Movie findMovieByTitle(final String s);
}
