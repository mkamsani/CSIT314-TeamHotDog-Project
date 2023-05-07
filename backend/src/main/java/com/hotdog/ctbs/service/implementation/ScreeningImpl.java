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


    // *** Havent done the testing for all methods ***
    // CRUD methods
    // 1. Create screening
    // *** Done the testing for this method ***
    @Override
    @Transactional
    public String createScreening(String movieTitle, String showTime, Boolean isActive, LocalDate showDate, Integer cinemaRoomId)
    {
        // checking flow
        // 1 ==> MOVIE (exists? && isActive?)
        // 2 ==> SHOW DATE (is valid?)
        // 3 ==> SHOW TIME (is valid?)
        // 4 ==> CINEMA ROOM (exists? && isActive? && is full? (4 timings only per date))
        // 5 ==> SCREENING (exists?)

        // check if movie exists
        Movie movie = movieRepo.findMovieByTitle(movieTitle);
        if (movie == null)
            throw new IllegalArgumentException("Movie does not exist.");
        else if(movie.isActive() == false)
            throw new IllegalArgumentException("Movie is not active.");

        System.out.println("Done checking movie");

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

        // check if cinema room exists
        CinemaRoom cinemaRoom = cinemaRoomRepo.findCinemaRoomById(cinemaRoomId);
        if (cinemaRoom == null)
            throw new IllegalArgumentException("Cinema room does not exist.");
        else if (cinemaRoom.getIsActive() == false)
            throw new IllegalArgumentException("Cinema room is not active.");

        // one cinema room only can have 4 showtimes in a day
        List<Screening> screenings = screeningRepo.findScreeningsByCinemaRoomIdAndShowDate(cinemaRoomId, showDate).orElse(null);
        if (screenings != null && screenings.size() >= 4) {
            throw new IllegalArgumentException("Cinema room is already full for the given date.");
        }

        System.out.println("Done checking cinema room");

        // showTime, cinemaRoom, showDate cannot be equal to each other.
        for (Screening screening : screeningRepo.findAll())
            if (screening.getShowTime().equals(showTime) && screening.getCinemaRoom().equals(cinemaRoomRepo.findCinemaRoomById(cinemaRoomId)) && screening.getShowDate().equals(showDate))
                throw new IllegalArgumentException("Screening already exists");

        System.out.println("Done checking any screening already exists");

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
    // get screening by id
    @Override
    @Transactional
    public Screening getScreeningById(UUID id)
    {
        return screeningRepo.findScreeningById(id);
    }

    // get all screenings (includes all inactive) for manager usage***
    // 1st order ==> show date in ascending order
    // 2nd order ==> show time in ascending order
    // 3rd order ==> cinema room in ascending order
    // 4th order ==> movie in ascending order
    @Override
    @Transactional
    public List<Screening> getAllScreenings()
    {
        return screeningRepo.findAllByOrderByShowDateAscShowTimeAscCinemaRoomAscMovieAsc();
    }

    // get all screenings by movie title follow the sort order
    @Override
    @Transactional
    public List<Screening> getAllScreeningsByMovieTitle(String movieTitle)
    {
        // check if movie exists
        Movie movie = movieRepo.findMovieByTitle(movieTitle);
        if (movie == null)
            throw new IllegalArgumentException("Movie does not exist.");

        return screeningRepo.findScreeningsByMovieTitleOrderByShowDateAscShowTimeAscCinemaRoomAscMovieAsc(movieTitle)
                .orElseThrow(() -> new IllegalArgumentException("No screenings found for this movie."));
    }


    // get all screenings by show date follow the sort order
    @Override
    @Transactional
    public List<Screening> getAllScreeningsByShowDate(LocalDate showDate)
    {
        return screeningRepo.findScreeningsByShowDateOrderByShowDateAscShowTimeAscCinemaRoomAscMovieAsc(showDate)
                .orElseThrow(() -> new IllegalArgumentException("No screenings found on this date."));
    }

    // get all screenings by show time follow the sort order
    @Override
    @Transactional
    public List<Screening> getAllScreeningsByShowTime(String showTime)
    {
        return screeningRepo.findScreeningsByShowTimeOrderByShowDateAscShowTimeAscCinemaRoomAscMovieAsc(showTime)
                .orElseThrow(() -> new IllegalArgumentException("No screenings found on this time."));
    }


    // get all screenings by cinema room id follow the sort order
    @Override
    @Transactional
    public List<Screening> getAllScreeningsByCinemaRoomId(Integer cinemaRoomId)
    {
        //check if cinema room exists
        CinemaRoom cinemaRoom = cinemaRoomRepo.findCinemaRoomById(cinemaRoomId);
        if (cinemaRoom == null)
            throw new IllegalArgumentException("Cinema room does not exist.");

        return screeningRepo.findScreeningsByCinemaRoomIdOrderByShowDateAscShowTimeAscCinemaRoomAscMovieAsc(cinemaRoomId)
                .orElseThrow(() -> new IllegalArgumentException("No screenings found for this cinema room."));
    }


    // get specific screening by movie id, show time, show date, cinema room id
    @Transactional
    public Screening getScreeningByMovieTitleAndShowTimeAndShowDateAndCinemaRoomId(String movieTitle, String showTime, LocalDate showDate, Integer cinemaRoomId)
    {
        // check using same format as createScreening method
        // check if movie exists
        Movie movie = movieRepo.findMovieByTitle(movieTitle);
        if (movie == null)
            throw new IllegalArgumentException("Movie does not exist.");

        // check if show date is valid
        if (showDate.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Invalid date.");

        // check if show time is valid
        if (!showTime.equals("morning")   &&
                !showTime.equals("afternoon") &&
                !showTime.equals("evening")   &&
                !showTime.equals("midnight"))
            throw new IllegalArgumentException("Invalid time.");

        // check if cinema room exists
        CinemaRoom cinemaRoom = cinemaRoomRepo.findCinemaRoomById(cinemaRoomId);
        if (cinemaRoom == null)
            throw new IllegalArgumentException("Cinema room does not exist.");
        else if (cinemaRoom.getIsActive() == false)
            throw new IllegalArgumentException("Cinema room is not active.");

        // check if screening exists
        Screening screening = screeningRepo.findScreeningByMovieTitleAndShowTimeAndShowDateAndCinemaRoomId(movieTitle, showTime, showDate, cinemaRoomId);
        if (screening == null)
            throw new IllegalArgumentException("Screening does not exist.");

        // found the screening
        return screening;

    }


    // get screening method for customers usage
    // ** customer should only able to see active screenings
    // 1st order ==> show date in ascending order
    // 2nd order ==> show time in ascending order
    // 3rd order ==> cinema room in ascending order
    // 4th order ==> movie in ascending order
    @Override
    @Transactional
    public List<Screening> getAllActiveScreenings()
    {
        return screeningRepo.findScreeningsByIsActiveOrderByShowDateAscShowTimeAscCinemaRoomAscMovieAsc(true)
                .orElseThrow(() -> new IllegalArgumentException("No active screenings found."));
    }

    // get all active screenings by movie title
    @Override
    @Transactional
    public List<Screening> getAllActiveScreeningsByMovieTitle(String movieTitle)
    {
        // check if movie exists
        Movie movie = movieRepo.findMovieByTitle(movieTitle);
        if (movie == null)
            throw new IllegalArgumentException("Movie does not exist.");

        return screeningRepo.findScreeningsByMovieTitleAndIsActiveOrderByShowDateAscShowTimeAscCinemaRoomAscMovieAsc(movieTitle, true)
                .orElseThrow(() -> new IllegalArgumentException("No active screenings found for this movie."));
    }

    @Override
    public String updateScreening(String movieTitle, String showTime, LocalDate showDate, CinemaRoom cinemaRoom) {
        return null;
    }

    // 3. Update screening ( got issue not completed)
    // update specific screening's movie title by movie title

   /* @Override
    @Transactional
    public String updateScreening(String movieTitle,
                                  String showTime,
                                  LocalDate showDate,
                                  CinemaRoom cinemaRoom)
    {

        List<Screening> screenings = screeningRepo.findAll();
        if (screenings != null && screenings.size() >= 4)
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
    }*/

    // 4. Delete screening (in progress need to check if it is linked to any ticket)
    @Override
    @Transactional
    public String deleteScreening(String movieTitle,
                                  String showTime,
                                  LocalDate showDate,
                                  CinemaRoom cinemaRoom) {
        //check if movie exists
        Movie movie = movieRepo.findMovieByTitle(movieTitle);
        if (movie == null)
            throw new IllegalArgumentException("Movie does not exist.");

        //check if cinema room exists
        if (cinemaRoomRepo.findCinemaRoomById(cinemaRoom.getId()).getId() > 8 || cinemaRoomRepo.findCinemaRoomById(cinemaRoom.getId()).getId() < 0)
            throw new IllegalArgumentException("Invalid cinema room.");

        //check if show time is valid
        if (!showTime.equals("morning") &&
                !showTime.equals("afternoon") &&
                !showTime.equals("evening") &&
                !showTime.equals("midnight"))
            throw new IllegalArgumentException("Invalid time.");

        //check if show date is valid
        if (showDate.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Invalid date.");

        boolean screeningExists = false;

        //check if screening exists
        // for loop screening list to check if screening exists
        for (Screening screening : screeningRepo.findAll())
            if (screening.getShowTime().equals(showTime) && screening.getCinemaRoom().equals(cinemaRoomRepo.findCinemaRoomById(cinemaRoom.getId())) && screening.getShowDate().equals(showDate)) {
                screeningExists = true;
                System.out.println("Screening exists");
                screeningRepo.delete(screening);
                return "Screening deleted.";
            }

        // return if there is no screening
        return "Screening does not exist.";

    }

    // Spare method
    /*// get all screenings by show date and show time
    @Override
    @Transactional
    public List<Screening> getScreeningsByDateAndTime(LocalDate localDate, String showTime){
        return screeningRepo.findScreeningsByShowDateAndShowTime(localDate, showTime)
                .orElseThrow(()
                        -> new IllegalArgumentException("No screenings found on this date."));
    }*/


}
