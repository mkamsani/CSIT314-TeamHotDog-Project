package com.hotdog.ctbs.controller.manager;

import com.hotdog.ctbs.service.implementation.TicketTypeImpl;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/manager/ticketType")
public class ManagerTicketTypeCreateController {

    private final TicketTypeImpl ticketTypeImpl;

    public ManagerTicketTypeCreateController(TicketTypeImpl ticketTypeImpl) {
        this.ticketTypeImpl = ticketTypeImpl;
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
