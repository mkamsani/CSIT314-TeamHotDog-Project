package com.hotdog.ctbs.service.implementation;

import com.hotdog.ctbs.entity.*;

import com.hotdog.ctbs.repository.*;
import com.hotdog.ctbs.service.TicketService;
import jakarta.transaction.Transactional;
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

        // get screening if null throw exception
        Screening screening = screeningRepo.findScreeningByMovieTitleAndShowTimeAndShowDateAndCinemaRoomId(
                movieTitle, showTime, showDate, cinemaRoomId);
        if (screening == null)
            throw new IllegalArgumentException("Screening does not exist");

        // check if screening is active
        if (!screening.getIsActive())
            throw new IllegalArgumentException("Screening is not active");

        // get seat if null throw exception (using row, column and cinema room)
        Seat seat = seatRepo.findSeatBySeatRowAndAndSeatColumnAndCinemaRoom(row, column, screening.getCinemaRoom());
        if (seat == null)
            throw new IllegalArgumentException("Seat does not exist");

        // check seat if it is booked
        List<Ticket> tickets = ticketRepo.findTicketsByScreening(screening);
        for (Ticket ticket : tickets) {
            if (ticket.getSeat().equals(seat))
                throw new IllegalArgumentException("Seat is already taken");
        }

        // get ticket type if null throw exception
        TicketType ticketType = ticketTypeRepo.findByTypeName(TicketTypeName);
        if (ticketType == null)
            throw new IllegalArgumentException("Ticket type does not exist");

        // rebuild updateduseraccount

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

    }

    // get all tickets
    @Transactional
    public List<Ticket> getAllTickets(){
        return ticketRepo.findAll();
    }




}
