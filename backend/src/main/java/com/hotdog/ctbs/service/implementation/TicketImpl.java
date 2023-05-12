package com.hotdog.ctbs.service.implementation;

import com.hotdog.ctbs.entity.*;

import com.hotdog.ctbs.repository.*;
import com.hotdog.ctbs.service.TicketService;
import jakarta.transaction.Transactional;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.List;

@Service
public class TicketImpl implements TicketService{

    final ScreeningRepository screeningRepo;
    final UserAccountRepository userAccountRepo;
    final TicketRepository ticketRepo;
    final SeatRepository seatRepo;
    final TicketTypeRepository ticketTypeRepo;
    //final LoyaltyPointRepository loyaltyPointRepo;

    public TicketImpl(ScreeningRepository screeningRepo,
                      UserAccountRepository userAccountRepo,
                      TicketRepository ticketRepo,
                      SeatRepository seatRepo,
                      TicketTypeRepository ticketTypeRepo)
    {
        this.screeningRepo = screeningRepo;
        this.userAccountRepo = userAccountRepo;
        this.ticketRepo = ticketRepo;
        this.seatRepo = seatRepo;
        this.ticketTypeRepo = ticketTypeRepo;
    }

    // create a ticket
    @Transactional
    public void createTicket(String userName,
                             String TicketTypeName,
                             String movieTitle, String showTime, LocalDate showDate, Integer cinemaRoomId,
                             char row, Integer column){


        // get user account if null throw exception
        UserAccount userAccount = userAccountRepo.findUserAccountByUsername(userName).orElse(null);
        if (userAccount == null)
            throw new IllegalArgumentException("User account does not exist");


        // check user account is active?
        if (!userAccount.getIsActive())
            throw new IllegalArgumentException("User account is not active");


        System.out.println("Done checking for user account");


        // get screening if null throw exception
        Screening screening = screeningRepo.findScreeningByMovieTitleAndShowTimeAndShowDateAndCinemaRoomId(
                movieTitle, showTime, showDate, cinemaRoomId);
        if (screening == null)
            throw new IllegalArgumentException("Screening does not exist");


        // check if screening is active
        if (!screening.getIsActive())
            throw new IllegalArgumentException("Screening is not active");

        System.out.println("Done checking for screening");


        // get seat if null throw exception (using row, column and cinema room)
        Seat seat = seatRepo.findSeatBySeatRowAndAndSeatColumnAndCinemaRoom(row, column, screening.getCinemaRoom());
        if (seat == null)
            throw new IllegalArgumentException("Seat does not exist");

        // check seat if it is booked or not
        List<Ticket> tickets = ticketRepo.findTicketsByScreening(screening).orElse(null);
        /*if (tickets == null || tickets.isEmpty())
            throw new IllegalArgumentException("Ticket does not exist");*/

        for (Ticket ticket : tickets) {
            if (ticket.getSeat().equals(seat))
                throw new IllegalArgumentException("Seat is already taken");
        }

        System.out.println("Done checking for seat");


        // get ticket type if null throw exception
        TicketType ticketType = ticketTypeRepo.findByTypeName(TicketTypeName);
        if (ticketType == null)
            throw new IllegalArgumentException("Ticket type does not exist");

        System.out.println("Done checking for ticket type");

        // create a ticket
        Ticket ticket = Ticket.builder()
                .id(UUID.randomUUID())
                .customer(userAccount)
                .screening(screening)
                .seat(seat)
                .ticketType(ticketType)
                .purchaseDate(OffsetDateTime.now())
                .build();


        ticketRepo.save(ticket);


        // inactive the screening if all seats are booked
        if (tickets.size() == screening.getCinemaRoom().getNumberOfSeats())
        {
            screening.setIsActive(false);
            screeningRepo.save(screening);
        }


    }




    // get all tickets
    @Transactional
    public List<Ticket> getAllTickets(){
        return ticketRepo.findAll();
    }

    // get all tickets by user account
    @Transactional
    public List<Ticket> getAllTicketsByUserAccount(String userName){
        UserAccount userAccount = userAccountRepo.findUserAccountByUsername(userName).orElse(null);
        if (userAccount == null)
            throw new IllegalArgumentException("User account does not exist");
        return ticketRepo.findTicketsByCustomer(userAccount);
    }

    // get all tickets by screening
    @Transactional
    public List<Ticket> getAllTicketsByScreening(String movieTitle, String showTime, LocalDate showDate, Integer cinemaRoomId){
        Screening screening = screeningRepo.findScreeningByMovieTitleAndShowTimeAndShowDateAndCinemaRoomId(
                movieTitle, showTime, showDate, cinemaRoomId);
        if (screening == null)
            throw new IllegalArgumentException("Screening does not exist");
        return ticketRepo.findTicketsByScreening(screening).orElse(null);
    }

    // update a ticket
    // Ticket type should not be updated
    @Transactional
    public void updateTicket(String userName, String currentMovieTitle, String newMovieTitle,
                             String currentShowTime, String newShowTime,
                             LocalDate currentShowDate, LocalDate newShowDate, Integer currentCinemaRoomId, Integer newCinemaRoomId,
                             char currentRow, char newRow, Integer currentColumn, Integer newColumn){
        // find currentScreening
        Screening currentScreening = screeningRepo.findScreeningByMovieTitleAndShowTimeAndShowDateAndCinemaRoomId(
                currentMovieTitle, currentShowTime, currentShowDate, currentCinemaRoomId);
        // find current Seats
        Seat currentSeat = seatRepo.findSeatBySeatRowAndAndSeatColumnAndCinemaRoom(currentRow, currentColumn, currentScreening.getCinemaRoom());
        // use currentScreening and currentSeats to find specific ticket
        Ticket currentTicket = ticketRepo.findTicketByScreeningAndSeat(currentScreening, currentSeat);

        UserAccount userAccount = userAccountRepo.findUserAccountByUsername(userName).orElse(null);
        if (userAccount == null)
            throw new IllegalArgumentException("User account does not exist");

        if (!userAccount.getIsActive())
            throw new IllegalArgumentException("User account is not active");

        // get screening if null throw exception
        Screening newScreening = screeningRepo.findScreeningByMovieTitleAndShowTimeAndShowDateAndCinemaRoomId(
                newMovieTitle, newShowTime, newShowDate, newCinemaRoomId);
        if (newScreening == null)
            throw new IllegalArgumentException("Screening does not exist");

        Seat newSeat = seatRepo.findSeatBySeatRowAndAndSeatColumnAndCinemaRoom(newRow, newColumn, newScreening.getCinemaRoom());
        if (newSeat == null)
            throw new IllegalArgumentException("Seat does not exist");

        // check if new seat in new screening is booked
        List<Ticket> tickets = ticketRepo.findTicketsByScreening(newScreening).orElse(null);
        for (Ticket ticket : tickets) {
            if (ticket.getSeat().equals(newSeat))
                throw new IllegalArgumentException("Seat is already taken");
        }

        // use new screening and new seat to update currentTicket
        // even if the values are the same
        // this results in less comparison and less code
        currentTicket.setSeat(newSeat);
        currentTicket.setScreening(newScreening);
    }


    /////////////////////////////////////*******************************************/////////////////////////////////////
    // Method to list out the available seats for a particular screening (important for customer to choose the seat)
    @Transactional
    public List<Seat> listAvailableSeats(String movieTitle, String showTime, LocalDate showDate, Integer cinemaRoomId){
        // get screening if null throw exception
        Screening screening = screeningRepo.findScreeningByMovieTitleAndShowTimeAndShowDateAndCinemaRoomId(
                movieTitle, showTime, showDate, cinemaRoomId);
        if (screening == null)
            throw new IllegalArgumentException("Screening does not exist");


        // check if screening is active
        if (!screening.getIsActive())
            throw new IllegalArgumentException("Screening is not active");

        // get all seats
        List<Seat> seats = seatRepo.findSeatsByCinemaRoom(screening.getCinemaRoom());

        // get all tickets
        List<Ticket> tickets = ticketRepo.findTicketsByScreening(screening).orElse(null);

        // remove booked seats
        for (Ticket ticket : tickets) {
            seats.remove(ticket.getSeat());
        }


        return seats;
    }



}
