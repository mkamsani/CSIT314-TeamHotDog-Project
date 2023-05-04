package com.hotdog.ctbs.repository;

import com.hotdog.ctbs.entity.Movie;
import com.hotdog.ctbs.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScreeningRepository extends JpaRepository<Screening, UUID> {

    Screening findScreeningById(UUID id);

    Optional<List<Screening>> findScreeningsByMovie(Movie movie);

    Optional<List<Screening>> findScreeningsByShowDate(LocalDate showDate);

    Optional<List<Screening>> findScreeningByShowTime(String showTime);

    Optional<List<Screening>> findScreeningsByIsActive(Boolean isActive);

    Optional<List<Screening>> findScreeningsByShowDateAndShowTime(LocalDate showDate, String showTime);

}
