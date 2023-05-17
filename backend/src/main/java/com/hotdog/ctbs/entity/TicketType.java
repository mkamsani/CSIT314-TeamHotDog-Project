package com.hotdog.ctbs.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotdog.ctbs.repository.TicketTypeRepository;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ticket_type")
public class TicketType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", nullable = false)
    protected UUID uuid;

    @Column(name = "type_name", length = 7)
    protected String typeName;

    @Column(name = "type_price", nullable = false, precision = 10, scale = 2)
    protected Double typePrice;

    @Column(name = "is_active")
    protected Boolean isActive;

    public static ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());


    public static String createTicketType(TicketTypeRepository ticketTypeRepository, String typeName, Double typePrice, Boolean isActive) {
        try {
            TicketType ticketType = new TicketType();
            ticketType.typeName = typeName;
            ticketType.typePrice = typePrice;
            ticketType.isActive = isActive;
            ticketTypeRepository.save(ticketType);
        }
        catch (Exception e) {
            return e.getMessage();
        }
        return "Ticket Type created successfully";
    }

    public static String updateTicketType(TicketTypeRepository ticketTypeRepository, String targetTypeName, String newTypeName, Double typePrice, Boolean isActive ){
        try {
            TicketType ticketType = new TicketType();
            ticketType = ticketTypeRepository.findByTypeName((targetTypeName)).orElse(null);
            if(ticketType == null){
                throw new IllegalArgumentException("Ticket Type not found");
            }

            if (newTypeName != null || newTypeName != "adult" || newTypeName != "child" || newTypeName != "senior" || newTypeName != "student" || newTypeName != "redemption") {
                ticketType.typeName = newTypeName;
            }
            if (typePrice != null) {
                ticketType.typePrice = typePrice;
            }

            ticketType.isActive = isActive;

            ticketTypeRepository.save(ticketType);
        }
        catch (Exception e) {
            return e.getMessage();
        }
        return "Ticket Type updated successfully";
    }

    public static String readTicketType(TicketTypeRepository ticketTypeRepository, String param) {
        List<TicketType> ticketTypeList = null;
        if (param.equals("all")) {
            ticketTypeList = ticketTypeRepository.findAll();
        }
        else if (param.equals("active")) {
            ticketTypeList = ticketTypeRepository.findAll().stream()
                    .filter(TicketType::getIsActive)
                    .toList();
        }
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (TicketType ticketType : ticketTypeList) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("typename", ticketType.getTypeName());
            objectNode.put("typeprice", ticketType.getTypePrice());
            objectNode.put("isactive", ticketType.getIsActive());
            arrayNode.add(objectNode);
        }

        return arrayNode.toString();
    }

    public static String suspendTicketType(TicketTypeRepository ticketTypeRepository , String targettypename) {
        TicketType ticketType = ticketTypeRepository.findByTypeName(targettypename).orElseThrow(
                () -> new IllegalArgumentException("Ticket Type not found.")
        );
        ticketType.isActive = false;
        ticketTypeRepository.save(ticketType);
        return "Successfully suspended ticket type";
    }
}