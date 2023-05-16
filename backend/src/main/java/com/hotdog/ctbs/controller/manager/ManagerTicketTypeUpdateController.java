package com.hotdog.ctbs.controller.manager;

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
 *   </pre></blockquote>
 *   </p>
 *
 *
 *   updateTicketType method will look for the targetTypeName and update all the fields of the ticket type.
 *   If the targetTypeName is not found, it will return an error message.
 *   If the targetTypeName is found, it will update the ticket type and return a success message.
 *   Adult, Child, Student, Senior and Redemption Types are not allowed to have their typeNames changed.
 *   If the targetTypeName is one of the above, it will return an error message.
 *   For now, update Method should allow user to update individual fields without having to update all fields.
 *   But this depends on whether json will allow passing null/empty values.
 */

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
      public void updateTicketType(final String targetTypeName,
                                 final String newTypeName,
                                 final Double newTypePrice,
                                 final Boolean newIsActive)
     */
    @PutMapping(value = "/update/{targetTicketTypeName}")
    public String ManagerUpdateTicketType(@RequestBody String json, @PathVariable String targetTicketTypeName)
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
            // delete later
            System.out.println("updateTicketType() failed");
            //
            return e.getMessage();
        }
        return "Ticket Type Updated";
    }
}
