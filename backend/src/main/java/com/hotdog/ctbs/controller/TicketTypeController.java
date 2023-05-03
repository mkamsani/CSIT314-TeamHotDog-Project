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

    @GetMapping("/read/getTicketTypeByTypeName")
    public String ReadTicketTypeByTypeName(@RequestParam String typeName)
    {
        return ticketTypeImpl.getTicketTypeByTypeName(typeName).toString();
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

    @PostMapping("/update/ticketType")
    public String UpdateTicketType(@RequestBody String json) {
            try{
                JsonNode jsonNode = new ObjectMapper().readTree(json);
                String targettypeName = jsonNode.get("targettypename").asText();
                String newTypeName = jsonNode.get("newtypename").asText();
                Double typePrice = jsonNode.get("typeprice").asDouble();
                Boolean isactive = jsonNode.get("isactive").asBoolean();
                ticketTypeImpl.updateTicketTypeByAllFields(targettypeName, newTypeName, typePrice, isactive);
                return "Success updating ticketType";
            }
            catch (Exception e){
                return "Error: " + e.getMessage();
            }
    }

    // change only the ticket type name
    @PostMapping("/update/ticketType/typename")
    public String updateTicketTypeByTypeName(@RequestBody String json) {
            try{
                JsonNode jsonNode = new ObjectMapper().readTree(json);
                String targettypeName = jsonNode.get("targettypename").asText();
                String newTypeName = jsonNode.get("newtypename").asText();
                ticketTypeImpl.updateTicketTypeByTypeName(targettypeName, newTypeName);
                return "Success updating ticketType";
            }
            catch (Exception e){
                return "Error: " + e.getMessage();
            }
    }

    // change only the ticket type price
    @PostMapping("/update/ticketType/typeprice")
    public String updateTicketTypeByTypePrice(@RequestBody String json) {
            try{
                JsonNode jsonNode = new ObjectMapper().readTree(json);
                String targettypeName = jsonNode.get("targettypename").asText();
                Double typePrice = jsonNode.get("typeprice").asDouble();
                ticketTypeImpl.updateTicketTypeByTypePrice(targettypeName, typePrice);
                return "Success updating ticketType";
            }
            catch (Exception e){
                return "Error: " + e.getMessage();
            }
    }

    // change only the ticket type isactive
    @PostMapping("/update/ticketType/isactive")
    public String updateTicketTypeByIsActive(@RequestBody String json) {
            try{
                JsonNode jsonNode = new ObjectMapper().readTree(json);
                String targettypeName = jsonNode.get("targettypename").asText();
                Boolean isactive = jsonNode.get("isactive").asBoolean();
                ticketTypeImpl.updateTicketTypeByIsActive(targettypeName, isactive);
                return "Success updating ticketType";
            }
            catch (Exception e){
                return "Error: " + e.getMessage();
            }
    }

    // delete ticket type by ticket type name
    @PostMapping("/delete/ticketType")
    public String DeleteTicketType(@RequestBody String json) {
            try{
                JsonNode jsonNode = new ObjectMapper().readTree(json);
                String targettypeName = jsonNode.get("targettypename").asText();
                ticketTypeImpl.deleteTicketTypeByTypeName(targettypeName);
                return "Success deleting ticketType";
            }
            catch (Exception e){
                return "Error: " + e.getMessage();
            }
    }
}
