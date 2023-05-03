package com.hotdog.ctbs.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotdog.ctbs.entity.Movie;
import com.hotdog.ctbs.entity.TicketType;
import com.hotdog.ctbs.service.implementation.MovieImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TicketTypeService {

    List<String> getAllTicketTypeNames();

    List<BigDecimal> getAllTicketTypePrices();

    List<Boolean> getAllTicketTypeIsActives();

    List<BigDecimal> getAllTicketTypePricesFromTicketTypeIsActives();

    List<String> getAllTicketTypeNamesFromTicketTypeIsActives();

    List<String> getAllTicketTypeNamesAndPricesFromTicketTypeIsActives();

    void createTicketType(String typeName, BigDecimal typePrice, Boolean isActive );

    void checkTicketTypeExistsByTypeName(String typeName);

    TicketType getTicketTypeByUUID(UUID uuid);

    TicketType getTicketTypeByTypeName(String typeName);

    void updateTicketTypeByTypename(String targetTypeName, String newTypeName);

    void updateTicketTypeByTypePrice(String targetTypeName, BigDecimal newTypePrice);

    void updateTicketTypeByIsActive(String targetTypeName, Boolean newIsActive);

    void deleteTicketTypeByTypeName(String typeName);
}
