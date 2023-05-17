package com.hotdog.ctbs.controller.customer;

import com.hotdog.ctbs.entity.Ticket;
import com.hotdog.ctbs.repository.TicketRepository;
import com.hotdog.ctbs.repository.UserAccountRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
