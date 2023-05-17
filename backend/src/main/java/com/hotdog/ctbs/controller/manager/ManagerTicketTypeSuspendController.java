package com.hotdog.ctbs.controller.manager;

import com.hotdog.ctbs.service.implementation.TicketTypeImpl;
import org.springframework.web.bind.annotation.*;

/**
 * The {@code ManagerTicketTypeSuspendController} class
 * exposes the {@code /api/manager/ticketType/suspend} endpoint
 *
 */


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/manager/ticketType")
public class ManagerTicketTypeSuspendController {

    private final TicketTypeImpl ticketTypeImpl;

    public ManagerTicketTypeSuspendController(TicketTypeImpl ticketTypeImpl) {
        this.ticketTypeImpl = ticketTypeImpl;
    }

    @DeleteMapping("/suspend/{targettypename}")
    public String Suspend(@PathVariable String targettypename) {
        try {
            ticketTypeImpl.suspend(targettypename);
            return "Success";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
