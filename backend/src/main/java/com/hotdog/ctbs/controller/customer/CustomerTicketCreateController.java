package com.hotdog.ctbs.controller.customer;

import com.hotdog.ctbs.entity.*;
import com.hotdog.ctbs.repository.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/customer/ticket")
public class CustomerTicketCreateController {
    private final UserAccountRepository userAccountRepository;

    private final TicketTypeRepository ticketTypeRepository;

    private final ScreeningRepository screeningRepository;

    private final SeatRepository seatRepository;

    private final ObjectMapper objectMapper;

    private final CinemaRoomRepository cinemaRoomRepository;

    private final LoyaltyPointRepository loyaltyPointRepository;
    private final TicketRepository ticketRepository;

    public CustomerTicketCreateController(UserAccountRepository userAccountRepository, TicketTypeRepository ticketTypeRepository, ScreeningRepository screeningRepository, SeatRepository seatRepository
                                  , CinemaRoomRepository cinemaRoomRepository,
                                          LoyaltyPointRepository loyaltyPointRepository,
                                          TicketRepository ticketRepository){
        this.userAccountRepository = userAccountRepository;
        this.ticketTypeRepository = ticketTypeRepository;
        this.screeningRepository = screeningRepository;
        this.seatRepository = seatRepository;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        this.cinemaRoomRepository = cinemaRoomRepository;
        this.loyaltyPointRepository = loyaltyPointRepository;
        this.ticketRepository = ticketRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<String> Create(@RequestBody final String json){
        System.out.println("CustomerTickerCreateController.Create() called");
        try{
            JsonNode jsonNode = objectMapper.readTree(json);
            Ticket.createTicket(userAccountRepository, ticketTypeRepository,
                            screeningRepository, seatRepository, loyaltyPointRepository,
                            cinemaRoomRepository,ticketRepository, jsonNode.get("userName").asText(),
                            jsonNode.get("ticketTypeName").asText(), jsonNode.get("showTime").asText(),
                            LocalDate.parse(jsonNode.get("showDate").asText()), jsonNode.get("cinemaRoomId").asInt(),
                            jsonNode.get("row").asText(), jsonNode.get("column").asInt(), jsonNode.get("isLoyaltyPointUsed").asBoolean());
            return ResponseEntity.ok("Success.");
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
