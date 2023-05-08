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
import java.util.List;

@Service
public class ScreeningImpl implements ScreeningService{

    final ScreeningRepository screeningRepo;
    final MovieRepository movieRepo;
    final CinemaRoomRepository cinemaRoomRepo;

    private static final List<String> SHOW_TIME_ORDER = Arrays.asList("morning", "afternoon", "evening", "midnight");

    public ScreeningImpl(ScreeningRepository screeningRepo,
                         MovieRepository movieRepo,
                         CinemaRoomRepository cinemaRoomRepo)
    {
        this.screeningRepo = screeningRepo;
        this.movieRepo = movieRepo;
        this.cinemaRoomRepo = cinemaRoomRepo;
    }

    // Compare method
    private static class ScreeningComparator implements Comparator<Screening> {

        // compare by date, then by time, then by cinema room, then by movie of screenings
        @Override
        public int compare(Screening s1, Screening s2) {
            int result = s1.getShowDate().compareTo(s2.getShowDate());
            if (result != 0) {
                return result;
            }
            result = SHOW_TIME_ORDER.indexOf(s1.getShowTime()) - SHOW_TIME_ORDER.indexOf(s2.getShowTime());
            if (result != 0) {
                return result;
            }
            result = s1.getCinemaRoom().compareTo(s2.getCinemaRoom());
            if (result != 0) {
                return result;
            }
            return s1.getMovie().compareTo(s2.getMovie());
        }
    }

    // CRUD methods
    // 1. Create screening
    @Override
    @Transactional
    public void createScreening(String movieTitle, String showTime, LocalDate showDate, Integer cinemaRoomId)
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
                .isActive(true)
                .showDate(showDate)
                .cinemaRoom(cinemaRoom)
                .build();

        screeningRepo.save(screening);

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
    public List<Screening> getAllScreenings() {
        List<Screening> screenings = screeningRepo.findAll();
        // sort the screening details using self defined comparator
        Collections.sort(screenings, new ScreeningComparator());
        return screenings;
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

        List<Screening> screenings = screeningRepo.findScreeningsByMovieTitle(movieTitle);
        // sort the screening details using self defined comparator
        Collections.sort(screenings, new ScreeningComparator());
        return screenings;
    }


    // get all screenings by show date follow the sort order
    @Override
    @Transactional
    public List<Screening> getAllScreeningsByShowDate(LocalDate showDate)
    {
        List<Screening> screenings = screeningRepo.findScreeningsByShowDate(showDate);
        // sort the screening details using self defined comparator
        Collections.sort(screenings, new ScreeningComparator());

        return screenings;
    }

    // get all screenings by show time follow the sort order
    @Override
    @Transactional
    public List<Screening> getAllScreeningsByShowTime(String showTime)
    {
        List<Screening> screenings = screeningRepo.findScreeningsByShowTime(showTime);
        // sort the screening details using self defined comparator
        Collections.sort(screenings, new ScreeningComparator());

        return screenings;
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

        List<Screening> screenings = screeningRepo.findScreeningsByCinemaRoom(cinemaRoom);
        // sort the screening details using self defined comparator
        Collections.sort(screenings, new ScreeningComparator());

        return screenings;
    }


    // get specific screening by movie id, show time, show date, cinema room id
    @Override
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

    // get all active screening method for customers usage
    // (customer does not need to see inactive screenings)
    // ** customer should only able to see active screenings
    // 1st order ==> show date in ascending order
    // 2nd order ==> show time in ascending order
    // 3rd order ==> cinema room in ascending order
    // 4th order ==> movie in ascending order
    @Override
    @Transactional
    public List<Screening> getAllActiveScreenings()
    {
        // get all active screenings
        List<Screening> screenings = screeningRepo.findScreeningsByIsActive(true);
        // sort the screening details using self defined comparator
        Collections.sort(screenings, new ScreeningComparator());

        return screenings;
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

        List<Screening> screenings = screeningRepo.findScreeningsByMovieTitleAndIsActive(movieTitle, true);
        if (screenings.isEmpty()) {
            throw new IllegalArgumentException("No active screenings found for this movie.");
        }
        else{
            // sort the screening details using self defined comparator
            Collections.sort(screenings, new ScreeningComparator());
            return screenings;
        }
    }

    // 3. Update screening
    // *** can update all attribute of a screening except the "isActive" (status)
    // update a screening require all 4 fields (movieTitle, showTime, showDate, cinemaRoomId)
    // if a screening is inactive, cant update
    @Override
    @Transactional
    public void updateScreening(String currentMovieTitle, String currentShowTime,
                                  LocalDate currentShowDate, Integer currentCinemaRoomId,
                                  String newMovieTitle, String newShowTime, LocalDate newShowDate, Integer newCinemaRoomId){

        // find current screening objects first
        Screening currentScreening = getScreeningByMovieTitleAndShowTimeAndShowDateAndCinemaRoomId(currentMovieTitle, currentShowTime, currentShowDate, currentCinemaRoomId);

        // check if current screening is active
        if (currentScreening.getIsActive() == false)
            throw new IllegalArgumentException("Cannot update a inactive screening.");

        // check if new movie title exists
        Movie newMovie = movieRepo.findMovieByTitle(newMovieTitle);
        if (newMovie == null)
            throw new IllegalArgumentException("New movie does not exist.");

        // check if new show date is valid
        if (newShowDate.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Invalid date.");

        // check if new show time is valid
        if (!newShowTime.equals("morning")   &&
                !newShowTime.equals("afternoon") &&
                !newShowTime.equals("evening")   &&
                !newShowTime.equals("midnight"))
            throw new IllegalArgumentException("Invalid time.");

        // check if new cinema room exists
        CinemaRoom newCinemaRoom = cinemaRoomRepo.findCinemaRoomById(newCinemaRoomId);
        if (newCinemaRoom == null)
            throw new IllegalArgumentException("New cinema room does not exist.");
        else if (newCinemaRoom.getIsActive() == false)
            throw new IllegalArgumentException("New cinema room is not active.");

        // showTime, cinemaRoom, showDate cannot be equal to each other.
        for (Screening screening : screeningRepo.findAll())
            if (screening.getShowTime().equals(newShowTime)
                    && screening.getCinemaRoom().equals(newCinemaRoom)
                    && screening.getShowDate().equals(newShowDate))
                throw new IllegalArgumentException("Screening already exists");


        // update screening details
        currentScreening.setMovie(newMovie);
        currentScreening.setShowTime(newShowTime);
        currentScreening.setShowDate(newShowDate);
        currentScreening.setCinemaRoom(newCinemaRoom);

        // save updated screening details
        screeningRepo.save(currentScreening);

        // print successful message
        System.out.println("Screening details updated successfully.");

    }

    // 4. Suspend the screening (update screening isActive to false)
    @Override
    @Transactional
    public void suspendScreeningByIsActive(String movieTitle,
                                  String currentShowTime,
                                  LocalDate curreshowDate,
                                  Integer cinemaRoomId, Boolean newIsActive) {

        // find current screening objects first (illegal argument exception if any invalid input)
        Screening currentScreening = getScreeningByMovieTitleAndShowTimeAndShowDateAndCinemaRoomId(
                movieTitle, currentShowTime, curreshowDate, cinemaRoomId);

        // check if current screening is active
        if (currentScreening.getIsActive() == false)
            throw new IllegalArgumentException("The screening is already suspended.");

        // update screening isActive to false
        currentScreening.setIsActive(newIsActive);

        // save updated screening details
        screeningRepo.save(currentScreening);

    }

    // Spare suspend method (not used) ==> expect one suspend button for each screening

}
