package com.hotdog.ctbs.implementation;

import com.hotdog.ctbs.entity.Screening;
import com.hotdog.ctbs.entity.Ticket;
import com.hotdog.ctbs.entity.Seat;
import com.hotdog.ctbs.entity.TicketType;
import com.hotdog.ctbs.entity.UserAccount;

import com.hotdog.ctbs.repository.*;

import com.hotdog.ctbs.service.TicketService;

import com.hotdog.ctbs.service.implementation.*;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@SpringBootTest
public class TicketTests {

    @Autowired
    private TicketImpl ticketImpl;

    @Test
    void createMethod(){

        // TEMPORARY PUT TRY STATEMENT HERE
        // ONCE HAS FAKE DATA THEN CAN REMOVE TRY STATEMENT

        try{
            // test create ticket
            // create a ticket
            for (int i = 1; i < 15; i++)
            {
                ticketImpl.createTicket("user_41",
                        "adult",
                        "morning",
                        LocalDate.of(2023, 5, 23),
                        1, 'A', i, false);
            }

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    @Test
    void getter(){

        // TEMPORARY PUT TRY STATEMENT HERE
        // ONCE HAS FAKE DATA THEN CAN REMOVE TRY STATEMENT
        try{
            // test get all tickets
            System.out.println("Testing getAllTickets method");
            System.out.println(ticketImpl.getAllTickets());
            System.out.println();

            // get all the available seats linked to screening
            System.out.println("Testing listAvailableSeats method");
            List<Seat> seats = ticketImpl.listAvailableSeats("evening",
                    LocalDate.of(2023, 5, 23), 1);
            System.out.println(seats.size());
            System.out.println(seats);
            System.out.println();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }


    }

//    void updateMethod(){
//
//        try{
//            //test to update any current ticket
//            System.out.println("Testing updateTicket method");
//            System.out.println(ticketImpl.updateTicket("dwallace",
//                    ));
//            System.out.println();
//        }
//
//    }


}
