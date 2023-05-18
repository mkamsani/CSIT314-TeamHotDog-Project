package com.hotdog.ctbs.controller.customer;

import com.fasterxml.jackson.databind.ObjectMapper;   // TODO : Remove
import com.fasterxml.jackson.databind.node.ArrayNode; // TODO : Remove
import com.hotdog.ctbs.entity.Ticket;
import com.hotdog.ctbs.repository.TicketRepository;
import com.hotdog.ctbs.repository.UserAccountRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;     // TODO : Remove
import java.util.ArrayList;     // TODO : Remove
import java.util.LinkedHashMap; // TODO : Remove
import java.util.List;          // TODO : Remove

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/customer/ticket")
public class CustomerTicketReadController {

    private final TicketRepository ticketRepo;
    private final UserAccountRepository userAccountRepo;

    public CustomerTicketReadController(TicketRepository ticketRepo, UserAccountRepository userAccountRepo)
    {
        this.ticketRepo = ticketRepo;
        this.userAccountRepo = userAccountRepo;
    }

    @GetMapping(value = "/read/{param}")
    public ResponseEntity<String> Read(@PathVariable final String param)
    {
        try {
            String json = Ticket.readTicket(userAccountRepo, ticketRepo, param);
            return ResponseEntity.ok().body(json);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // TODO : Split this into a new controller, and possibly a new User Story.
    // curl -X GET -H "Content-Type: application/json" http://localhost:8000/api/customer/ticket/read-booking/2023-12-31/66/afternoon
    @GetMapping(value = "/read-booking/{date}/{room}/{time}")
    public ResponseEntity<String> ReadBooking(@PathVariable final String date, @PathVariable final String room, @PathVariable final String time)
    {
        try {
            // Declare an anonymous arraylist
            List<LinkedHashMap> list = new ArrayList<>();
            // Add the list of available seats to the arraylist
            // list.addAll(ticketRepo.findAvailableSeats(LocalDate.parse(date), Integer.parseInt(room), time));
            ArrayNode arrayNode = new ObjectMapper().createArrayNode();
            for (LinkedHashMap map : list) {
                arrayNode.addPOJO(map);
            }
            System.out.println(arrayNode.toString());
            return ResponseEntity.ok().body(list.toString());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
