package com.hotdog.ctbs.controller.manager;

import com.hotdog.ctbs.service.implementation.TicketTypeImpl;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/manager/ticketType")
public class ManagerTicketTypeReadController {

    private final TicketTypeImpl ticketTypeImpl;

    public ManagerTicketTypeReadController(TicketTypeImpl ticketTypeImpl) {
        this.ticketTypeImpl = ticketTypeImpl;
    }

    /*
      RetriveController Method will return either all ticket types or a specific ticket type.
      It will make use of a switch statement to determine which method to call.
      Method to call will be determined by whether it is case All or default (specific ticket type).
     */
    @GetMapping(value = "/read/{param}")
    public String ManagerReadTicketType(@PathVariable final String param)
    {
        try {
            switch (param) {

                case "all" -> {
                    return ticketTypeImpl.getAllTicketTypes().toString();
                }
                default -> {
                    return String.valueOf(ticketTypeImpl.getTicketTypeByTypeName(param));
                }
            }
        }
        catch (Exception e){
            return "Error: " + e.getMessage();
        }
    }
}
