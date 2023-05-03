package com.hotdog.ctbs.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotdog.ctbs.entity.TicketType;
;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TicketTypeService {

    List<TicketType> getAllTicketTypesDetails();

    List<String> getAllTicketTypeNames();

    List<Double> getAllTicketTypePrices();

    List<Boolean> getAllTicketTypeIsActives();

    List<Double> getAllTicketTypePricesFromTicketTypeIsActives();

    List<String> getAllTicketTypeNamesFromTicketTypeIsActives();

    List<String> getAllTicketTypeNamesAndPricesFromTicketTypeIsActives();

    void createTicketType(String typeName, Double typePrice, Boolean isActive );

    void checkTicketTypeExistsByTypeName(String typeName);

    TicketType getTicketTypeByTypeName(String typeName);

    void updateTicketTypeByTypeName(String targetTypeName, String newTypeName);

    void updateTicketTypeByTypePrice(String targetTypeName, Double newTypePrice);

    void updateTicketTypeByIsActive(String targetTypeName, Boolean newIsActive);

    // update Ticket_Type by all fields, method will take targetTypeName, newTypeName, newTypePrice, newIsActive as input
    void updateTicketTypeByAllFields(String targetTypeName, String newTypeName, Double newTypePrice, Boolean newIsActive);

    void deleteTicketTypeByTypeName(String typeName);
}
