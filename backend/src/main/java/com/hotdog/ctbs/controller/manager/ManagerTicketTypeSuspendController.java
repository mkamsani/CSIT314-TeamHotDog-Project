package com.hotdog.ctbs.controller.manager;

import com.hotdog.ctbs.entity.TicketType;
import com.hotdog.ctbs.repository.TicketTypeRepository;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
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

    private final TicketTypeRepository TicketTypeRepository;

    public ManagerTicketTypeSuspendController(TicketTypeRepository ticketTypeRepository) {
        this.TicketTypeRepository = ticketTypeRepository;
    }

    @DeleteMapping("/suspend/{targettypename}")
    public ResponseEntity<String>Suspend(@PathVariable String targettypename) {
        try {
            return ResponseEntity.ok(TicketType.suspendTicketType(TicketTypeRepository, targettypename));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
