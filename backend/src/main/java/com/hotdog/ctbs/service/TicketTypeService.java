package com.hotdog.ctbs.service;


import com.hotdog.ctbs.entity.TicketType;
import jakarta.transaction.Transactional;

import java.util.List;


public interface TicketTypeService {


    @Transactional
    TicketType getTicketTypeByName(String typeName);

    List<TicketType> getAllTicketTypeIsActives();

    List<TicketType> getAllTicketTypes();

    void createTicketType(String typeName, Double typePrice, Boolean isActive );

    void updateTicketType(String targetTypeName, String newTypeName, Double newTypePrice, Boolean newIsActive);


}
