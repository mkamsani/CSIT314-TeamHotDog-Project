package com.hotdog.ctbs.service.implementation;

import com.hotdog.ctbs.entity.*;
import com.hotdog.ctbs.repository.*;
import com.hotdog.ctbs.service.ScreeningService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.List;

@Service
public class ScreeningImpl implements ScreeningService {

    final ScreeningRepository screeningRepo;
    final MovieRepository movieRepo;
    final CinemaRoomRepository cinemaRoomRepo;

    final TicketRepository ticketRepo;

    final LoyaltyPointRepository loyaltyPointRepo;

    private static final List<String> SHOW_TIME_ORDER = Arrays.asList("morning", "afternoon", "evening", "midnight");


    /*private enum status {
        active,
        suspended,
        cancelled
    }
*/
    public ScreeningImpl(ScreeningRepository screeningRepo,
                         MovieRepository movieRepo,
                         CinemaRoomRepository cinemaRoomRepo,
                         TicketRepository ticketRepo,
                         LoyaltyPointRepository loyaltyPointRepo) {
        this.screeningRepo = screeningRepo;
        this.movieRepo = movieRepo;
        this.cinemaRoomRepo = cinemaRoomRepo;
        this.ticketRepo = ticketRepo;
        this.loyaltyPointRepo = loyaltyPointRepo;
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
            result = s1.getCinemaRoom().getId().compareTo(s2.getCinemaRoom().getId());
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
    public void createScreening(String movieTitle, String showTime, LocalDate showDate, Integer cinemaRoomId) {
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
        else if (movie.isActive() == false)
//            throw new IllegalArgumentException("Movie is not active.");

            System.out.println("Done checking movie");

        // Screening's date cannot be in the past.
        if (showDate.isBefore(LocalDate.now())) {
            System.out.println("showDate: " + showDate);
            System.out.println("LocalDate.now(): " + LocalDate.now());
            throw new IllegalArgumentException("Invalid date.");
        }

        System.out.println("Done checking if show date is valid");

        if (!showTime.equals("morning") &&
                !showTime.equals("afternoon") &&
                !showTime.equals("evening") &&
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
                .status("active")
                .showDate(showDate)
                .cinemaRoom(cinemaRoom)
                .build();

        screeningRepo.save(screening);

    }

    // 2. Read screening
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
    public List<Screening> getAllScreeningsByMovieTitle(String movieTitle) {
        // check if movie exists
        Movie movie = movieRepo.findMovieByTitle(movieTitle);
        if (movie == null)
            throw new IllegalArgumentException("Movie does not exist.");

        List<Screening> screenings = screeningRepo.findScreeningsByMovieTitle(movieTitle).orElse(null);

        if (screenings.isEmpty() || screenings == null) {
            throw new IllegalArgumentException("No screenings found for the specified movie.");
        }

        // sort the screening details using self defined comparator
        Collections.sort(screenings, new ScreeningComparator());
        return screenings;
    }


    // get all screenings by show date follow the sort order
    @Override
    @Transactional
    public List<Screening> getAllScreeningsByShowDate(LocalDate showDate) {

        List<Screening> screenings = screeningRepo.findScreeningsByShowDate(showDate).orElse(null);

        if (screenings.isEmpty() || screenings == null) {
            throw new IllegalArgumentException("No screenings found for the specified date.");
        }


        // sort the screening details using self defined comparator
        Collections.sort(screenings, new ScreeningComparator());

        return screenings;
    }

    // get all screenings by show time follow the sort order
    @Override
    @Transactional
    public List<Screening> getAllScreeningsByShowTime(String showTime) {

        List<Screening> screenings = screeningRepo.findScreeningsByShowTime(showTime).orElse(null);

        if (screenings.isEmpty() || screenings == null) {
            throw new IllegalArgumentException("No screenings found for the specified time.");
        }

        // sort the screening details using self defined comparator
        Collections.sort(screenings, new ScreeningComparator());

        return screenings;
    }


    // get all screenings by cinema room id follow the sort order
    @Override
    @Transactional
    public List<Screening> getAllScreeningsByCinemaRoomId(Integer cinemaRoomId) {
        //check if cinema room exists
        CinemaRoom cinemaRoom = cinemaRoomRepo.findCinemaRoomById(cinemaRoomId);
        if (cinemaRoom == null)
            throw new IllegalArgumentException("Cinema room does not exist.");


        List<Screening> screenings = screeningRepo.findScreeningsByCinemaRoom(cinemaRoom).orElse(null);

        if (screenings.isEmpty() || screenings == null) {
            throw new IllegalArgumentException("No screenings found for the specified cinema room.");
        }

        // sort the screening details using self defined comparator
        Collections.sort(screenings, new ScreeningComparator());

        return screenings;
    }


    // get specific screening by movie id, show time, show date, cinema room id
    @Override
    @Transactional
    public Screening getScreeningByShowTimeAndShowDateAndCinemaRoomId(String showTime, LocalDate showDate, Integer cinemaRoomId) {
        // check using same format as createScreening method
        // check if movie exists
        /*Movie movie = movieRepo.findMovieByTitle(movieTitle);
        if (movie == null)
            throw new IllegalArgumentException("Movie does not exist.");*/

        /* Debugging
        // check if show date is valid
        if (showDate.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Invalid date.");*/

        // check if show time is valid
        if (!showTime.equals("morning") &&
                !showTime.equals("afternoon") &&
                !showTime.equals("evening") &&
                !showTime.equals("midnight"))
            throw new IllegalArgumentException("Invalid time.");

        // check if cinema room exists
        CinemaRoom cinemaRoom = cinemaRoomRepo.findCinemaRoomById(cinemaRoomId);
        if (cinemaRoom == null)
            throw new IllegalArgumentException("Cinema room does not exist.");
        else if (cinemaRoom.getIsActive() == false)
            throw new IllegalArgumentException("Cinema room is not active.");

        // check if screening exists
        /*Screening screening = screeningRepo.findScreeningByMovieTitleAndShowTimeAndShowDateAndCinemaRoomId(movieTitle, showTime, showDate, cinemaRoomId);*/
        Screening screening = screeningRepo.findScreeningByShowTimeAndShowDateAndCinemaRoomId(showTime, showDate, cinemaRoomId);
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
    public List<Screening> getAllActiveScreenings() {

        // get all active screenings with local date today
        List<Screening> screenings = screeningRepo.findActiveScreeningsLaterOrEqual(LocalDate.now());
        // = screeningRepo.findScreeningsByIsActive(true).orElse(null);

        if (screenings.isEmpty() || screenings == null) {
            throw new IllegalArgumentException("No active screenings found.");
        }

        // sort the screening details using self defined comparator
        Collections.sort(screenings, new ScreeningComparator());

        return screenings;
    }

    // get all active screenings by movie title (condition getStatus == "active")
    // (customer does not need to see inactive screenings)
    @Override
    @Transactional
    public List<Screening> getAllActiveScreeningsByMovieTitle(String movieTitle) {

        // check if movie exists
        Movie movie = movieRepo.findMovieByTitle(movieTitle);
        if (movie == null)
            throw new IllegalArgumentException("Movie does not exist.");

        List<Screening> screenings = screeningRepo.findActiveScreeningsForMovieAndLaterOrEqual(LocalDate.now(), movie);

        if (screenings.isEmpty() || screenings == null) {
            throw new IllegalArgumentException("No active screenings found for the specified movie.");
        }

        return screenings;
    }


    // 3. Update screening
    // *** can update all attribute of a screening except the "isActive" (status)
    // update a screening require all 4 fields (movieTitle, showTime, showDate, cinemaRoomId)
    // if a screening is inactive, cant update
    @Override
    @Transactional
    public void updateScreening(String currentShowTime,
                                LocalDate currentShowDate, Integer currentCinemaRoomId,
                                String newMovieTitle, String newShowTime, LocalDate newShowDate, Integer newCinemaRoomId) {

        // find current screening objects first
        Screening currentScreening = getScreeningByShowTimeAndShowDateAndCinemaRoomId(currentShowTime, currentShowDate, currentCinemaRoomId);

        // check current screening if it is before now
        if (currentScreening.getShowDate().isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Cannot update a screening that has already passed.");

        System.out.println("Start checking for the current screenings.");
        System.out.println(currentScreening.getStatus());
        // check if current screening status(string) is suspended or cancelled then throw illegal argument
        // if (currentScreening.getStatus() == Status.suspended || currentScreening.getStatus() == Status.cancelled)
        if (currentScreening.getStatus().equals("suspended") || currentScreening.getStatus().equals("cancelled"))
        {
            throw new IllegalArgumentException("Cannot update a screening that is suspended or cancelled.");
        }

        System.out.println("Done checking for the current screenings.");
        // check if new movie title exists
        Movie newMovie = movieRepo.findMovieByTitle(newMovieTitle);
        if (newMovie == null)
            throw new IllegalArgumentException("New movie does not exist.");

        // check if new show date is valid
        if (newShowDate.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Cannot set the date to past.");

        // check if new show time is valid
        if (!newShowTime.equals("morning") &&
                !newShowTime.equals("afternoon") &&
                !newShowTime.equals("evening") &&
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
                throw new IllegalArgumentException("You cannot update to a " +
                        "screening that has the same show time, cinema room and show date as another screening.");


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


    @Override
    @Transactional
    public void suspendScreening(String currentShowTime,
                                 LocalDate currentShowDate,
                                 Integer cinemaRoomId) {

        // find current screening objects first (illegal argument exception if any invalid input)
        Screening currentScreening = getScreeningByShowTimeAndShowDateAndCinemaRoomId(
                currentShowTime, currentShowDate, cinemaRoomId);

        // check if current screening is already and cannot suspend cancel movie
        if (currentScreening.getStatus().equals("suspended") || currentScreening.getStatus().equals("cancelled"))
            throw new IllegalArgumentException("The screening is already suspended or cancelled.");

        // update screening isActive to false
        currentScreening.setStatus("suspended");

        // save updated screening details
        screeningRepo.save(currentScreening);

    }

    // 15 / 5 add on cancel screening
    // cancel the screening then whoever booked the ticket for that screening
    // will get loyalty points
    @Override
    @Transactional
    public void cancelScreening(String currentShowTime,
                                LocalDate currentShowDate,
                                Integer cinemaRoomId) {

        // find current screening objects first (illegal argument exception if any invalid input)
        Screening currentScreening = getScreeningByShowTimeAndShowDateAndCinemaRoomId(
                currentShowTime, currentShowDate, cinemaRoomId);

        // check if current screening is already cancel and cannot cancel movie
        if (currentScreening.getStatus().equals("cancelled"))
            throw new IllegalArgumentException("The screening is already cancelled.");

        // update screening isActive to false
        currentScreening.setStatus("cancelled");

        // save updated screening details
        screeningRepo.save(currentScreening);

        // get all the tickets for the screening
        Optional<List<Ticket>> tickets = ticketRepo.findTicketsByScreening(currentScreening);

        // if there are no tickets,
        if (tickets.isPresent()) {


            // for each ticket, get the customer and add loyalty points
            for (Ticket ticket : tickets.get()) {
                UserAccount customer = ticket.getCustomer();
                Optional<LoyaltyPoint> loyaltyPoint = loyaltyPointRepo.findByUserAccountUsername(customer.getUsername());
                if (loyaltyPoint.isPresent()) {
                    LoyaltyPoint loyaltyPoint1 = loyaltyPoint.get();
                    loyaltyPoint1.setPointsTotal(loyaltyPoint1.getPointsTotal() + 1000);
                    loyaltyPointRepo.save(loyaltyPoint1);
                }

            }

        }


    }
}
