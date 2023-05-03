package com.hotdog.ctbs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotdog.ctbs.entity.Movie;
import com.hotdog.ctbs.repository.MovieRepository;
import com.hotdog.ctbs.service.implementation.TicketTypeImpl;
import jakarta.servlet.http.HttpServletRequest;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import com.hotdog.ctbs.service.implementation.MovieImpl;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/ticketType")
public class TicketTypeController {
    private final TicketTypeImpl ticketTypeImpl;

    public TicketTypeController(TicketTypeImpl ticketTypeImpl)
    {
        this.ticketTypeImpl = ticketTypeImpl;
    }

    // To show a list of all ticket types (including active and inactive ticket types)
    @GetMapping("/read/allTicketTypes")
    public String ReadAllTicketTypes()
    {
        return ticketTypeImpl.getAllTicketTypeNames().toString();
    }

    // To show a list of all TicketType objects (including active and inactive ticket types)
    @GetMapping("/read/allTicketTypesDetails")
    public String ReadAllTicketTypesDetails()
    {
        return ticketTypeImpl.getAllTicketTypesDetails().toString();
    }

    @GetMapping("/read/allTicketTypeNames")
    public String ReadAllTicketTypeNames()
    {
        return ticketTypeImpl.getAllTicketTypeNames().toString();
    }

    @GetMapping("/read/allTicketTypePrices")
    public String ReadAllTicketTypePrices()
    {
        return ticketTypeImpl.getAllTicketTypePrices().toString();
    }

    @PostMapping("/create/ticketType")
    public String CreateTicketType(@RequestBody String json) {
            try{
                JsonNode jsonNode = new ObjectMapper().readTree(json);
                String typeName = jsonNode.get("typename").asText();
                Double typePrice = jsonNode.get("typeprice").asDouble();
                Boolean isactive = jsonNode.get("isactive").asBoolean();
                ticketTypeImpl.createTicketType(typeName, typePrice, isactive);
                return "Success creating ticketType";
            }
            catch (Exception e){
                return "Error: " + e.getMessage();
            }
        }
    }

    @PostMapping("/update/ticketType")
    public String UpdateTicketType(@RequestBody String json) {
            try{
                JsonNode jsonNode = new ObjectMapper().readTree(json);
                String typeName = jsonNode.get("typename").asText();
                Double typePrice = jsonNode.get("typeprice").asDouble();
                Boolean isactive = jsonNode.get("isactive").asBoolean();
                ticketTypeImpl.updateTicketType(typeName, typePrice, isactive);
                return "Success updating ticketType";
            }
            catch (Exception e){
                return "Error: " + e.getMessage();
            }
        }
    }
