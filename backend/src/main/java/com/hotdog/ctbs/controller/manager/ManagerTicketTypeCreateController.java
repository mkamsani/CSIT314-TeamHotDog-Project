package com.hotdog.ctbs.controller.manager;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotdog.ctbs.service.implementation.TicketTypeImpl;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The {@code ManagerTicketTypeReadController} class
 * exposes the {@code /api/manager/ticketType/read} endpoint
 *
 * <p>
 *     The returned JSON format is:
 *     <blockquote><pre>
 *     [
 *      {
 *          "typename": "adult",
 *          "typeprice": 10.0,
 *          "isactive": true
 *       },
 *       {
 *           "typename": "child",
 *           "typeprice": 5.0,
 *           "isactive": true
 *        }
 *       ]
 *
 */

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/manager/ticketType")
public class ManagerTicketTypeCreateController {

    private final TicketTypeImpl ticketTypeImpl;

    private final ObjectMapper objectMapper;

    public ManagerTicketTypeCreateController(TicketTypeImpl ticketTypeImpl, ObjectMapper objectMapper) {
        this.ticketTypeImpl = ticketTypeImpl;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }


    /** Create a {@code TicketType} based on the given JSON. */
    @PostMapping("/create/ticketType")
    public String CreateTicketType(@RequestBody final String json) {
        try{
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String typeName = jsonNode.get("typename").asText();
            Double typePrice = jsonNode.get("typeprice").asDouble();
            Boolean isactive = jsonNode.get("isactive").asBoolean();
            ticketTypeImpl.createTicketType(typeName, typePrice, isactive);
        }
        catch (Exception e){
            return e.getMessage();
        }
        return "Success creating ticketType";
    }

}
