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

    public static void createTicketType(TicketTypeRepository ticketTypeRepository, String typeName, Double typePrice, Boolean isActive) {
        if (typeName == null)
            throw new IllegalArgumentException("Type name cannot be null.");

        if (typeName.equals("adult") ||
            typeName.equals("child") ||
            typeName.equals("senior") ||
            typeName.equals("student") ||
            typeName.equals("redemption"))
            throw new IllegalArgumentException("Type name is reserved.");

        for (String typeNameFromRepository : ticketTypeRepository.findAllTypeName())
            if (typeName.equalsIgnoreCase(typeNameFromRepository))
                throw new IllegalArgumentException("Type name already exists.");

        TicketType ticketType = new TicketType();
        ticketType.typeName = typeName;
        ticketType.typePrice = typePrice;
        ticketType.isActive = isActive;
        ticketTypeRepository.save(ticketType);
    }
    // typeName has issue
    // if i update child to childUpdated while ticketPrice is different it will still update
    // if new Type Name is blank when updating then it will turn black
    // update do further testing
    public static void updateTicketType(TicketTypeRepository ticketTypeRepository, String targetTypeName, String newTypeName, Double typePrice, Boolean isActive){
        TicketType ticketType = ticketTypeRepository.findByTypeName(targetTypeName).orElse(null);
        if (ticketType == null)
            throw new IllegalArgumentException("Ticket Type not found");
        if (newTypeName == null)
            throw new IllegalArgumentException("Invalid Type Name");

        if (newTypeName.equals("adult") ||
                newTypeName.equals("child") ||
                newTypeName.equals("senior") ||
                newTypeName.equals("student") ||
                newTypeName.equals("redemption")) {
            throw new IllegalArgumentException("Reserved Ticket Types");
        }

        for (String typeNameFromRepository : ticketTypeRepository.findAllTypeName())
            if (newTypeName.equalsIgnoreCase(typeNameFromRepository))
                throw new IllegalArgumentException("Type name already exists.");

        if (typePrice == null || typePrice < 0)
            throw new IllegalArgumentException();

        ticketType.typePrice = typePrice;
        ticketType.typeName = newTypeName;

        ticketTypeRepository.save(ticketType);
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
    // suspend no issue
    public static void suspendTicketType(TicketTypeRepository ticketTypeRepository , String targettypename) {
        TicketType ticketType = ticketTypeRepository.findByTypeName(targettypename).orElseThrow(
                () -> new IllegalArgumentException("Ticket Type not found.")
        );
        ticketType.isActive = false;
        ticketTypeRepository.save(ticketType);
    }
}