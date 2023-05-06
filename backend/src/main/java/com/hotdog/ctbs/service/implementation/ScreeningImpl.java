package com.hotdog.ctbs.service.implementation;

import com.hotdog.ctbs.entity.Screening;
import com.hotdog.ctbs.entity.Movie;
import com.hotdog.ctbs.entity.CinemaRoom;
import com.hotdog.ctbs.repository.*;
import com.hotdog.ctbs.service.ScreeningService;
import jakarta.transaction.Transactional;
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

    // CRUD methods
    // 1. Create screening
    // *** Done the testing for this method ***
    @Override
    @Transactional
    public String createScreening(String movieTitle, String showTime, Boolean isActive, LocalDate showDate, Integer cinemaRoomId)
    {
        // check if movie exists
        Movie movie = movieRepo.findMovieByTitle(movieTitle);
        if (movie == null)
            throw new IllegalArgumentException("Movie does not exist.");

        System.out.println("Done checking movie");

        // check if cinema room exists
        CinemaRoom cinemaRoom = cinemaRoomRepo.findCinemaRoomById(cinemaRoomId);
        if (cinemaRoom == null)
            throw new IllegalArgumentException("Cinema room does not exist.");
        else if (cinemaRoom.getIsActive() == false)
            throw new IllegalArgumentException("Cinema room is not active.");

        System.out.println("Done checking cinema room");

        // showTime, cinemaRoom, showDate cannot be equal to each other.
        for (Screening screening : screeningRepo.findAll())
            if (screening.getShowTime().equals(showTime) && screening.getCinemaRoom().equals(cinemaRoomRepo.findCinemaRoomById(cinemaRoomId)) && screening.getShowDate().equals(showDate))
                throw new IllegalArgumentException("Screening already exists");

        System.out.println("Done checking any screening already exists");

        // one cinema room only can have 4 showtimes in a day
        List<Screening> screenings = screeningRepo.findScreeningsByShowDate(showDate).orElse(null);
        if (screenings != null && screenings.size() > 4)
            throw new IllegalArgumentException("Cinema room is full.");

        System.out.println("Done checking cinema room is full");

        // Screening's date cannot be in the past.
        if (showDate.isBefore(LocalDate.now()))
        {
            System.out.println("showDate: " + showDate);
            System.out.println("LocalDate.now(): " + LocalDate.now());
            throw new IllegalArgumentException("Invalid date.");
        }

        System.out.println("Done checking if show date is valid");


        if (!showTime.equals("morning")   &&
                !showTime.equals("afternoon") &&
                !showTime.equals("evening")   &&
                !showTime.equals("midnight"))
            throw new IllegalArgumentException("Invalid time.");

        System.out.println("Done checking if screening time is valid");


        Screening screening = Screening.builder()
                .id(UUID.randomUUID())
                .movie(movie)
                .showTime(showTime)
                .isActive(isActive)
                .showDate(showDate)
                .cinemaRoom(cinemaRoom)
                .build();

        screeningRepo.save(screening);

        return null;
    }

    // 2. Read screening
    // get all screenings
    @Override
    @Transactional
    public List<Screening> getAllScreenings(){
        return screeningRepo.findAll();
    }





    // 3. Update screening
    // 4. Delete screening



    @Override
    public List<Screening> getScreeningsByDate(LocalDate localDate)
    {
        return screeningRepo.findScreeningsByShowDate(localDate)
                            .orElseThrow(() -> new IllegalArgumentException("No screenings found on this date."));
    }


    public List<Screening> getScreeningsByDateAndTime(LocalDate localDate, String showTime){
        return screeningRepo.findScreeningsByShowDateAndShowTime(localDate, showTime)
                            .orElseThrow(()
                             -> new IllegalArgumentException("No screenings found on this date."));
    }



    @Override
    public String updateScreening(Movie movie, String showTime, Boolean isActive, LocalDate showDate, CinemaRoom cinemaRoom) {
        return null;
    }


    // get screening by id
    public Screening getScreeningById(UUID id)
    {
        return screeningRepo.findScreeningById(id);
    }


    // get screening by movie id, show time, show date, cinema room id
    @Transactional
    public Screening getScreeningByMovieTitleAndShowTimeAndShowDateAndCinemaRoomId(String movieTitle, String showTime, LocalDate showDate, Integer cinemaRoomId)
    {
        //check if movie exists
        Movie movie = movieRepo.findMovieByTitle(movieTitle);
        if (movie == null)
            throw new IllegalArgumentException("Movie does not exist.");

        //check if cinema room exists
        if (cinemaRoomRepo.findCinemaRoomById(cinemaRoomId).getId() > 8 || cinemaRoomRepo.findCinemaRoomById(cinemaRoomId).getId() < 0)
            throw new IllegalArgumentException("Invalid cinema room.");

        //check if show time is valid
        if (!showTime.equals("morning")   &&
                !showTime.equals("afternoon") &&
                !showTime.equals("evening")   &&
                !showTime.equals("midnight"))
            throw new IllegalArgumentException("Invalid time.");

        //check if show date is valid
        if (showDate.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Invalid date.");

        boolean screeningExists = false;

        //check if screening exists
        // for loop screening list to check if screening exists
        for (Screening screening : screeningRepo.findAll())
            if (screening.getShowTime().equals(showTime) && screening.getCinemaRoom().equals(cinemaRoomRepo.findCinemaRoomById(cinemaRoomId)) && screening.getShowDate().equals(showDate))
            {
                screeningExists = true;
                System.out.println("Screening exists");
                return screening;
            }

        // return null if there is no screening
        return null;

    }

    @Override
    public String updateScreening(String movieTitle,
                                  String showTime,
                                  LocalDate showDate,
                                  CinemaRoom cinemaRoom)
    {

        List<Screening> screenings = screeningRepo.findAll();
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


        for (Screening existingScreening : screeningRepo.findAll()){
            if (existingScreening.getShowTime().equals(showTime) && existingScreening.getCinemaRoom().equals(cinemaRoom) && existingScreening.getShowDate().equals(showDate))
            {
                System.out.println("Screening Exists");
                existingScreening.setMovie(movieRepo.findMovieByTitle(movieTitle));
                screeningRepo.save(existingScreening);
                return "Update Successful";
            }
            else
            {
                throw new IllegalArgumentException("Screening does not exist");
            }
        }
        return null;
    }

}
