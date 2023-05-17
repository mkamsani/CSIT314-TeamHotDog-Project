package com.hotdog.ctbs.controller.manager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotdog.ctbs.entity.TicketType;
import com.hotdog.ctbs.repository.TicketTypeRepository;
import org.springframework.web.bind.annotation.*;

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

    private final TicketTypeRepository ticketTypeRepository;
    private final ObjectMapper objectMapper;

    public ManagerTicketTypeCreateController(TicketTypeRepository ticketTypeRepository) {
        this.ticketTypeRepository = ticketTypeRepository;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }


    /** Create a {@code TicketType} based on the given JSON. */
    @PostMapping("/create/ticketType")
    public void Create(@RequestBody final String json) {
        try{
            JsonNode jsonNode = objectMapper.readTree(json);
            String typeName = jsonNode.get("typename").asText();
            Double typePrice = jsonNode.get("typeprice").asDouble();
            Boolean isactive = jsonNode.get("isactive").asBoolean();
            TicketType.createTicketType(ticketTypeRepository, typeName, typePrice, isactive);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
