package com.hotdog.ctbs.service.implementation;

import com.hotdog.ctbs.entity.TicketType;
import com.hotdog.ctbs.repository.TicketTypeRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class TicketTypeImpl {
    final
    TicketTypeRepository ticketTypeRepository;

    public TicketTypeImpl(TicketTypeRepository ticketTypeRepository) {
        this.ticketTypeRepository = ticketTypeRepository;
    }

    //create new Ticket_Type
    // new ticketType typeName must not be the same as any other existing Ticket Types in database (1st check)
    // allow overlapping ticket price
    public void createTicketType(String typeName, BigDecimal typePrice, Boolean isActive ){
        TicketType ticketType = new TicketType();
        for (TicketType existingTicketType : ticketTypeRepository.findAll()) {
            if (existingTicketType.getTypeName().equals(typeName)) {
                throw new IllegalStateException("Ticket Type already exists");
            }
        }
        ticketType.setTypeName(typeName);
        ticketType.setTypePrice(typePrice);
        ticketType.setIsActive(isActive);
        ticketTypeRepository.save(ticketType);
    }

    // check if TicketType exists by typeName and throw exception if exists
    public void checkTicketTypeExistsByTypeName(String typeName){
        for (TicketType existingTicketType : ticketTypeRepository.findAll()) {
            if (existingTicketType.getTypeName().equals(typeName)) {
                throw new IllegalStateException("Ticket Type already exists");
            }
        }
    }
    // retrieve Ticket_type by UUID
    public TicketType getTicketTypebyUUID(UUID uuid){
        TicketType ticketType = ticketTypeRepository.findById(uuid).orElse(null);
        return ticketType;
    }

    // find ticketType by typeName
    public TicketType getTicketTypeByTypeName(String typeName){
        TicketType ticketType = ticketTypeRepository.findByTypeName(typeName);
        return ticketType;
    }

    //update Ticket_Type by typeName, method will take targetTypeName and newTypeName as input
    public void updateTicketTypeByTypename(String targetTypeName, String newTypeName){
        checkTicketTypeExistsByTypeName(newTypeName);
        TicketType ticketType = ticketTypeRepository.findByTypeName(targetTypeName);
        ticketType.setTypeName(newTypeName);
        ticketTypeRepository.save(ticketType);
    }

    //update Ticket_Type by typePrice, method will take targetTypeName and newTypePrice as input
    public void updateTicketTypeByTypePrice(String targetTypeName, BigDecimal newTypePrice){
        checkTicketTypeExistsByTypeName(targetTypeName);
        TicketType ticketType = ticketTypeRepository.findByTypeName(targetTypeName);
        ticketType.setTypePrice(newTypePrice);
        ticketTypeRepository.save(ticketType);
    }


}
