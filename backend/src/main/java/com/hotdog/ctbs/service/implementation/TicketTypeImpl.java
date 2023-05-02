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

    // create new Ticket_Type
    /*
    public void createTicketType(String typeName, BigDecimal typePrice, Boolean isActive ){
        // new ticketType typeName must not be the same as any other existing Ticket Types in database (1st check)
        // allow overlapping ticket price
        for (String existing)
    }*/

    public void createTicketType(String typeName, BigDecimal typePrice, Boolean isActive ){
        TicketType ticketType = new TicketType();
        ticketType.setTypeName(typeName);
        ticketType.setTypePrice(typePrice);
        ticketType.setIsActive(isActive);
        ticketTypeRepository.save(ticketType);
    }



    // retrieve Ticket_type by UUID
    public TicketType getTicketTypebyUUID(UUID uuid){
        TicketType ticketType = ticketTypeRepository.findById(uuid).orElse(null);
        return ticketType;
    }


}
