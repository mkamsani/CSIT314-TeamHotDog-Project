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
    final LoyaltyPointRepository loyaltyPointRepo;

    public TicketImpl(ScreeningRepository screeningRepo,
                      UserAccountRepository userAccountRepo,
                      TicketRepository ticketRepo,
                      SeatRepository seatRepo,
                      TicketTypeRepository ticketTypeRepo,
                      LoyaltyPointRepository loyaltyPointRepo)
    {
        this.screeningRepo = screeningRepo;
        this.userAccountRepo = userAccountRepo;
        this.ticketRepo = ticketRepo;
        this.seatRepo = seatRepo;
        this.ticketTypeRepo = ticketTypeRepo;
        this.loyaltyPointRepo = loyaltyPointRepo;
    }

    // points to redeem for a ticket (1 point = 1 dollar) ==> might follow redemption from Ticket Type?
    private final Integer loyaltyPointsToRedeemTicket = 10;


    // create a ticket
    // so far this method pass thru testing based on the design now
    // it updates the total of redeem point of loyalty point also done checking total available point
    @Transactional
    public void createTicket(String userName,
                             String TicketTypeName,
                             String movieTitle, String showTime, LocalDate showDate, Integer cinemaRoomId,
                             char row, Integer column,
                             boolean isLoyaltyPointUsed){


        // get user account if null throw exception
        UserAccount userAccount = userAccountRepo.findUserAccountByUsername(userName).orElse(null);
        if (userAccount == null)
            throw new IllegalArgumentException("User account does not exist");

        // check user account is active?
        if (!userAccount.getIsActive())
            throw new IllegalArgumentException("User account is not active");

        // check if user account is customer
        if (!userAccount.getUserProfile().getPrivilege().equals("customer"))
            throw new IllegalArgumentException("User account is not customer");

        System.out.println("Done checking for user account");


        // get screening if null throw exception
        Screening screening = screeningRepo.findScreeningByMovieTitleAndShowTimeAndShowDateAndCinemaRoomId(
                movieTitle, showTime, showDate, cinemaRoomId);
        if (screening == null)
            throw new IllegalArgumentException("Screening does not exist");

        // check if screening is suspended or cancelled
        if (screening.getStatus().equals("suspended"))
            throw new IllegalArgumentException("Screening is suspended");
        else if (screening.getStatus().equals("cancelled"))
            throw new IllegalArgumentException("Screening is cancelled");

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

        // check if loyalty point is used
        if (isLoyaltyPointUsed){

            // get loyalty point if null throw exception
            LoyaltyPoint loyaltyPointForUser = loyaltyPointRepo.findByUserAccountUsername(userName).orElse(null);
            if (loyaltyPointForUser == null)
                throw new IllegalArgumentException("Loyalty point does not exist");

            // check if loyalty point is enough
            if ((loyaltyPointForUser.getPointsTotal() - loyaltyPointForUser.getPointsRedeemed()) < loyaltyPointsToRedeemTicket)
                throw new IllegalArgumentException("Loyalty point is not enough");

            // update loyalty point for user (redeem points increase)
            loyaltyPointForUser.setPointsRedeemed(loyaltyPointForUser.getPointsRedeemed() + loyaltyPointsToRedeemTicket);
            loyaltyPointRepo.save(loyaltyPointForUser);

            // update ticket type (assign to particular redemption ticket type)
            ticketType = ticketTypeRepo.findByTypeName("redemption");

        }
        else{
            // add one loyalty point for user
            LoyaltyPoint loyaltyPointForUser = loyaltyPointRepo.findByUserAccountUsername(userName).orElse(null);
            if (loyaltyPointForUser == null)
                throw new IllegalArgumentException("Loyalty point does not exist");

            loyaltyPointForUser.setPointsTotal(loyaltyPointForUser.getPointsTotal() + 1);
            loyaltyPointRepo.save(loyaltyPointForUser);

        }

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

    /////////////////////////////////////*******************************************/////////////////////////////////////
    // Method to list out the available seats for a particular screening (important for customer to choose the seat)
    // but hardly to know this method will be used by which controller
    @Transactional
    public List<Seat> listAvailableSeats(String movieTitle, String showTime, LocalDate showDate, Integer cinemaRoomId){
        // get screening if null throw exception
        Screening screening = screeningRepo.findScreeningByMovieTitleAndShowTimeAndShowDateAndCinemaRoomId(
                movieTitle, showTime, showDate, cinemaRoomId);
        if (screening == null)
            throw new IllegalArgumentException("Screening does not exist");


        // check if screening is active
        if (screening.getStatus() == "suspended")
            throw new IllegalArgumentException("Screening is not active");

        // check if screening is cancelled
        if (screening.getStatus() == "cancelled")
            throw new IllegalArgumentException("Screening is cancelled");

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

    //can remove later
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

   /* // return a list of ticket by particular date
    @Transactional
    public List<Ticket> getTicketsByDate(LocalDate date){
        // check if any ticket exists
        List<Ticket> ticketList = ticketRepo.findTicketsByPurchaseDate(date).orElse(null);
        if (ticketList == null)
            throw new IllegalArgumentException("No ticket exists");
        return ticketList;
    }

    // return a list of ticket from one date to another date (weekly)
    @Transactional
    public List<Ticket> getTicketsByDateRange(LocalDate startDate, LocalDate endDate){
        // check if any ticket exists
        List<Ticket> ticketList = ticketRepo.findTicketsByPurchaseDateBetween(startDate, endDate);
        if (ticketList == null)
            throw new IllegalArgumentException("No ticket exists");
        return ticketList;
    }

    // return a list of ticket by particular month
    @Transactional
    public List<Ticket> getTicketsByMonth(Integer month){
        // check if any ticket exists
        List<Ticket> ticketList = ticketRepo.findTicketsByPurchaseDateMonth(month).orElse(null);
        if (ticketList == null)
            throw new IllegalArgumentException("No ticket exists");
        return ticketList;
    }*/





}
