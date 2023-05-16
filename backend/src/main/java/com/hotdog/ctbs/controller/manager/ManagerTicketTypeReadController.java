package com.hotdog.ctbs.controller.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotdog.ctbs.entity.TicketType;
import com.hotdog.ctbs.service.implementation.TicketTypeImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    private final TicketTypeImpl ticketTypeImpl;

    private final ObjectMapper objectMapper;

    public ManagerTicketTypeReadController(TicketTypeImpl ticketTypeImpl, ObjectMapper objectMapper) {
        this.ticketTypeImpl = ticketTypeImpl;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    /**
     * Read a JSON array of {@code TicketType} object(s)
     */
    @GetMapping(value = "/read/{param}")
    public ResponseEntity<String> Read(@PathVariable final String param)
    {
        try {
            List<TicketType> ticketTypeList = switch(param) {
                case "active" ->
                        ticketTypeImpl.getAllTicketTypeIsActives();
                case "all" ->
                        ticketTypeImpl.getAllTicketTypes();
                default ->
                        List.of(ticketTypeImpl.getTicketTypeByName(param));
            };
            ArrayNode arrayNode = objectMapper.createArrayNode();
            for (TicketType ticketType : ticketTypeList) {
                ObjectNode objectNode = objectMapper.createObjectNode();
                objectNode.put("typename", ticketType.getTypeName());
                objectNode.put("typeprice", ticketType.getTypePrice());
                objectNode.put("isactive", ticketType.getIsActive());
                arrayNode.add(objectNode);
            }
            return ResponseEntity.ok(objectMapper.writeValueAsString(arrayNode));

        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
