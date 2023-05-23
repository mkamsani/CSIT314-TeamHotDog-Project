package com.hotdog.ctbs.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotdog.ctbs.repository.TicketTypeRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

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

    @Transient
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    //////////////////////////////// Service /////////////////////////////////

    public static void createTicketType(TicketTypeRepository ticketTypeRepository, String typeName, Double typePrice) {
        if (typeName == null)
            throw new IllegalArgumentException("Type name cannot be null.");

        if (typeName.equals("adult") ||
            typeName.equals("child") ||
            typeName.equals("senior") ||
            typeName.equals("student") ||
            typeName.equals("redemption"))
            throw new IllegalArgumentException("Type name is reserved.");

        for (String typeNameFromRepository : ticketTypeRepository.findAll().stream().map(TicketType::getTypeName).toList())
            if (typeName.equalsIgnoreCase(typeNameFromRepository))
                throw new IllegalArgumentException("Type name already exists.");

        TicketType ticketType = new TicketType();
        ticketType.typeName = typeName;
        ticketType.typePrice = typePrice;
        ticketType.isActive = true;
        System.out.println("Saving ticketType");
        ticketTypeRepository.save(ticketType);
    }

    public static void updateTicketType(TicketTypeRepository ticketTypeRepository, String targetTypeName, String newTypeName, Double typePrice){
        System.out.println("called updateTicketType");
        TicketType ticketType = ticketTypeRepository.findByTypeName(targetTypeName).orElse(null);
        if (ticketType == null)
            throw new IllegalArgumentException("Ticket Type not found");
        if (newTypeName == null)
            throw new IllegalArgumentException("Invalid Type Name");

        for (String typeNameFromRepository : ticketTypeRepository.findAll().stream().map(TicketType::getTypeName).toList())
            if (newTypeName.equalsIgnoreCase(typeNameFromRepository))
                throw new IllegalArgumentException("Ticket Type already exists.");


        // for reserved targetticketTypes, if newTypeName is empty, check if typePrice is valid, if valid change only typePrice
        switch(targetTypeName){
            case "adult", "child", "senior", "student", "redemption" -> {
                switch (newTypeName) {
                    case "" -> {
                        if (typePrice == null || typePrice < 0 || typePrice == 0)
                            throw new IllegalArgumentException("Invalid Type Price");
                        else {
                            System.out.println("Reserved TypeName : Change to typePrice without change to typeName" + typePrice);
                            ticketType.typeName = targetTypeName;
                            ticketType.typePrice = typePrice;
                            ticketTypeRepository.save(ticketType);
                        }
                    }
                    default -> {
                        throw new IllegalArgumentException("Reserved Ticket Types");
                    }
                }
            }
            // for all other ticket type names, if newTypeName is empty, check if typePrice is valid, if valid change only typePrice
            // else if newTypeName is not empty, check if typePrice is valid, if valid change both typeName and typePrice
            default -> {
                switch (newTypeName) {
                    case "" -> {
                        if (typePrice == null || typePrice < 0)
                            throw new IllegalArgumentException("Invalid Type Price");
                        else {
                            System.out.println("Non-reserved targettypename: Change to typePrice" + typePrice);
                            ticketType.typeName = targetTypeName;
                            ticketType.typePrice = typePrice;
                            ticketTypeRepository.save(ticketType);
                        }
                    }
                    case "adult", "child", "senior", "student", "redemption" -> {
                        throw new IllegalArgumentException("Reserved Ticket Types");
                    }
                    default -> {
                        if (typePrice == null || typePrice < 0)
                            throw new IllegalArgumentException("Invalid Type Price");
                        else if(typePrice == 0) {
                            System.out.println("Non-reserved targettypename: No change to typePrice" + ticketType.typePrice);
                            ticketType.typeName = newTypeName;
                            ticketType.typePrice = ticketType.typePrice;
                        }
                        else {
                            System.out.println("Non-reserved targettypename: Change to typePrice" + typePrice);
                            ticketType.typeName = newTypeName;
                            ticketType.typePrice = typePrice;
                        }
                        ticketTypeRepository.save(ticketType);
                    }
                }
            }
        }
    }

    public static String readTicketType(TicketTypeRepository ticketTypeRepository, String param) {
        System.out.println("called ReadTicket");
        List<TicketType> ticketTypeList;
        if (param.equals("all"))
            ticketTypeList = ticketTypeRepository.findAll();
        else if (param.equals("active")) // Used by customer.
            ticketTypeList = ticketTypeRepository.findAllByIsActiveTrue();
        else
            throw new IllegalArgumentException("Invalid parameter.");

        ArrayNode an = objectMapper.createArrayNode();
        for (TicketType ticketType : ticketTypeList) {
            if (param.equals("active") && ticketType.typeName.equals("redemption")) continue; // Customer should not see redemption ticket type.
            ObjectNode on = objectMapper.createObjectNode();
            on.put("typename", ticketType.typeName);
            on.put("typeprice", ticketType.typePrice);
            on.put("isactive", ticketType.isActive.toString());
            an.add(on);
        }
        return an.toString();
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