package com.hotdog.ctbs.controller.manager;

import com.hotdog.ctbs.entity.TicketType;
import com.hotdog.ctbs.repository.TicketTypeRepository;
import org.springframework.http.ResponseEntity;
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
 *     </pre></blockquote>
 *     A singular TicketType object would be wrapped in an array:
 *     <blockquote><pre>
 *     [
 *         {
 *         "typename": "adult",
 *         "typeprice": 10.0,
 *         "isactive": true
 *         }
 *      ]
 *      </pre></blockquote>
 * </p>
 *
 *
 */


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/manager/ticketType")
public class ManagerTicketTypeReadController {

    private final TicketTypeRepository TicketTypeRepository;

    public ManagerTicketTypeReadController(TicketTypeRepository ticketTypeRepository) {
        TicketTypeRepository = ticketTypeRepository;
    }

    /**
     * Read a JSON array of {@code TicketType} object(s)
     */
    @GetMapping(value = "/read/{param}")
    public ResponseEntity<String> Read(@PathVariable final String param)
    {
        try {

            return ResponseEntity.ok(TicketType.readTicketType(TicketTypeRepository, param));

        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
