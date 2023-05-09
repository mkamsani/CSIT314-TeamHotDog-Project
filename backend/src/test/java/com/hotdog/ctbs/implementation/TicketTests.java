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


        // test create ticket
        // create a ticket
        ticketImpl.createTicket("jim",
                "adult",
                "Spider-Man", "morning",
                LocalDate.of(2025, 5, 1),
                1, 'A', 20);

    }

    @Test
    void getter(){

        // test get all tickets
        System.out.println("Testing getAllTickets method");
        System.out.println(ticketImpl.getAllTickets());
        System.out.println();
    }




}
