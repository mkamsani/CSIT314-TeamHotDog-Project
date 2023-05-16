package com.hotdog.ctbs.service.implementation;

import com.hotdog.ctbs.entity.TicketType;
import com.hotdog.ctbs.repository.TicketTypeRepository;
import com.hotdog.ctbs.service.TicketTypeService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

@Service
public class TicketTypeImpl implements TicketTypeService {
    final TicketTypeRepository ticketTypeRepository;

    public TicketTypeImpl(TicketTypeRepository ticketTypeRepository) {
        this.ticketTypeRepository = ticketTypeRepository;
    }

    // get ticket type by name
    @Override
    @Transactional
    public TicketType getTicketTypeByName(final String typeName){
        return ticketTypeRepository.findByTypeName(typeName).orElseThrow(
                () -> new IllegalArgumentException("Ticket Type not found.")
        );
    }

    // return a list of all ticket type isActives
    @Override
    public List<TicketType> getAllTicketTypeIsActives(){
        return ticketTypeRepository.findAll().stream()
                .filter(TicketType::getIsActive)
                .toList();
    }

    // return a list of all ticket type details
    @Override
    public List<TicketType> getAllTicketTypes(){
        return ticketTypeRepository.findAll();
    }


    //create new Ticket_Type
    // new ticketType typeName must not be the same as any other existing Ticket Types in database (1st check)
    // allow overlapping ticket price
    @Override
    public void createTicketType(String typeName, Double typePrice, Boolean isActive ){
        //checkTicketTypeExistsByTypeName(typeName);
        ticketTypeRepository.save(TicketType.builder()
                                                .uuid(UUID.randomUUID())
                                                .typeName(typeName)
                                                .typePrice(typePrice)
                                                .isActive(isActive)
                                                .build()
        );
    }


    //update Ticket_Type by typeName, method will take targetTypeName and update all details about it
    @Override
    public void updateTicketType(final String targetTypeName,
                                 final String newTypeName,
                                 final Double newTypePrice,
                                 final Boolean newIsActive)
    {
        System.out.println("checking for target TicketType name");

        TicketType ticketType = ticketTypeRepository.findByTypeName(targetTypeName).orElseThrow(
                () -> new IllegalArgumentException("Ticket Type not found.")
        );

        System.out.println("checking new TicketType name");

        //check if newTypeName is the same as any other existing Ticket Types in database (1st check)
        if (ticketTypeRepository.findByTypeName(newTypeName).isPresent() &&
                !ticketType.getTypeName().equals(newTypeName))
            throw new IllegalArgumentException("Ticket Type name already exists.");

        System.out.println("checking new TicketType name format");

        if (!newTypeName.matches("[a-zA-Z0-9]+"))
            throw new IllegalArgumentException("Ticket Type name must be alphanumeric.");

        System.out.println("checking new TicketType name reserved");

        if (newTypeName.equals("adult") || newTypeName.equals("child") ||
                newTypeName.equals("student") || newTypeName.equals("senior")
                || newTypeName.equals("redemption"))
            throw new IllegalArgumentException("TicketType" + newTypeName + " is reserved");

        ticketType.setTypeName(newTypeName);

        if(newTypePrice != null)
            ticketType.setTypePrice(newTypePrice);

        if(newIsActive != null)
            ticketType.setIsActive(newIsActive);
    }

    public String suspend(String targetTypeName) {
        TicketType ticketType = ticketTypeRepository.findByTypeName(targetTypeName).orElse(null);
        if (ticketType == null)
            return "Not found";
        if(!ticketType.getIsActive())
            return targetTypeName + " is already suspended";

        ticketType.setIsActive(false);
        ticketTypeRepository.save(ticketType);

        int size = ticketTypeRepository.findAll()
                    .stream()
                .filter(TicketType::getIsActive)
                .toList()
                .size();
        if (size == 0)
            return targetTypeName + " has been suspended. No active ticket types left.";
        else
            return targetTypeName + " has been suspended." + size + " active ticket types left.";
    }
}
