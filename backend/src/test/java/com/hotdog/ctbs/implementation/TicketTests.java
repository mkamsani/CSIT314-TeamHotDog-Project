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

        try{
            // test create ticket
            // create a ticket
            for (int i = 1; i < 10; i++)
            {
                ticketImpl.createTicket("user_41",
                        "adult",
                        "Spider-Man", "morning",
                        LocalDate.of(2025, 5, 1),
                        1, 'A', i);
            }

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    @Test
    void getter(){

        try{
            // test get all tickets
            System.out.println("Testing getAllTickets method");
            System.out.println(ticketImpl.getAllTickets());
            System.out.println();

            // get all the available seats linked to screening
            // List<Seat> listAvailableSeats(String movieTitle, String showTime, LocalDate showDate, Integer cinemaRoomId)
            // {"movie":"Spider-Man","showTime":"morning","isActive":true,"showDate":"2023-01-01","cinemaRoom":1}
            System.out.println("Testing listAvailableSeats method");
            List<Seat> seats = ticketImpl.listAvailableSeats("Spider-Man", "morning",
                    LocalDate.of(2025, 5, 1), 1);
            System.out.println(seats.size());
            System.out.println(seats);
            System.out.println();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }


    }


}
