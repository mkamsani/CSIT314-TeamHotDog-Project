package com.hotdog.ctbs.controller.manager;

import com.hotdog.ctbs.service.implementation.TicketTypeImpl;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/manager/ticketType")
public class ManagerTicketTypeUpdateController {
    private final TicketTypeImpl ticketTypeImpl;
    public ManagerTicketTypeUpdateController(TicketTypeImpl ticketTypeImpl) {
        this.ticketTypeImpl = ticketTypeImpl;
    }
    /*
      UpdateController Method will update a specific ticket type.
     */
    @PutMapping(value = "/update/{targetTypeName}")
    public String ManagerUpdateTicketType(@RequestBody String json, @PathVariable final String targetTicketTypeName)
    {
        System.out.println("TicketTypeUpdateController.UpdateTicketType is called");
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);

            ticketTypeImpl.updateTicketType(
                    targetTicketTypeName,
                    jsonNode.get("ticketTypeName").asText(),
                    jsonNode.get("ticketTypePrice").asDouble(),
                    jsonNode.get("ticketTypeIsActive").asBoolean()
            );
        }
        catch (Exception e){
            return "Error: " + e.getMessage();
        }
        return "Ticket Type Updated";
    }
}
