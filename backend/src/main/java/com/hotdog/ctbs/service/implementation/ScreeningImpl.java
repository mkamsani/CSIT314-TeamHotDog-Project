package com.hotdog.ctbs.service.implementation;

import com.hotdog.ctbs.entity.Screening;
import com.hotdog.ctbs.entity.Movie;
import com.hotdog.ctbs.entity.CinemaRoom;
import com.hotdog.ctbs.repository.*;
import com.hotdog.ctbs.service.ScreeningService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ScreeningImpl implements ScreeningService{

    final ScreeningRepository screeningRepo;
    final MovieRepository movieRepo;
    final CinemaRoomRepository cinemaRoomRepo;

    public ScreeningImpl(ScreeningRepository screeningRepo,
                         MovieRepository movieRepo,
                         CinemaRoomRepository cinemaRoomRepo)
    {
        this.screeningRepo = screeningRepo;
        this.movieRepo = movieRepo;
        this.cinemaRoomRepo = cinemaRoomRepo;
    }

    // get all screenings
    @Override
    public List<Screening> getAllScreenings(){
        return screeningRepo.findAll();
    }

    @Override
    public List<Screening> getScreeningsByDate(LocalDate localDate)
    {
        return screeningRepo.findScreeningsByShowDate(localDate)
                            .orElseThrow(() -> new IllegalArgumentException("No screenings found on this date."));
    }

    public List<Screening> getScreeningsByDateAndTime(LocalDate localDate, String showTime){
        return null;
    }

    @Override
    public String createScreening(String movieTitle, String showTime, Boolean isActive, LocalDate showDate,
                                  CinemaRoom cinemaRoom)
    {
        // showTime, cinemaRoom, showDate cannot be equal to each other.
        for (Screening screening : screeningRepo.findAll())
            if (screening.getShowTime().equals(showTime) && screening.getCinemaRoom().equals(cinemaRoom) && screening.getShowDate().equals(showDate))
                throw new IllegalArgumentException("Screening already exists");

        // one cinema room only 4 showtimes in a day
        List<Screening> screenings = screeningRepo.findScreeningsByShowDate(showDate).orElse(null);
        if (screenings != null && screenings.size() > 4)
            throw new IllegalArgumentException("Cinema room is full.");

        // Screening's date cannot be in the past.
        if (showDate.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Invalid date.");

        if (!showTime.equals("morning")   &&
            !showTime.equals("afternoon") &&
            !showTime.equals("evening")   &&
            !showTime.equals("midnight"))
            throw new IllegalArgumentException("Invalid time.");

        if (cinemaRoom.getId() > 8 || cinemaRoom.getId() < 0)
            throw new IllegalArgumentException("Invalid cinema room.");

        Screening screening = Screening.builder()
                                       .id(UUID.randomUUID())
                                       .movie(movieRepo.findMovieByTitle(movieTitle))
                                       .showTime(showTime)
                                       .isActive(isActive)
                                       .showDate(showDate)
                                       .cinemaRoom(cinemaRoom)
                                       .build();

        screeningRepo.save(screening);

        return null;
    }

    @Override
    public String updateScreening(Movie movie,
                                  String showTime,
                                  Boolean isActive,
                                  LocalDate showDate,
                                  CinemaRoom cinemaRoom)
    {
        return null;
    }
}
