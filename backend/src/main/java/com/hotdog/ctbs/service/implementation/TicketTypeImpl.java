package com.hotdog.ctbs.service.implementation;

import com.hotdog.ctbs.entity.TicketType;
import com.hotdog.ctbs.repository.TicketTypeRepository;
import com.hotdog.ctbs.service.TicketTypeService;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

@Service
public class TicketTypeImpl implements TicketTypeService {
    final TicketTypeRepository ticketTypeRepository;

    public TicketTypeImpl(TicketTypeRepository ticketTypeRepository) {
        this.ticketTypeRepository = ticketTypeRepository;
    }

    // return a list of all ticket type names
    @Override
    public List<String> getAllTicketTypeNames(){
        return ticketTypeRepository.findAll().stream()
                .map(TicketType::getTypeName)
                .toList();
    }

    // return a list of all ticket type prices
    @Override
    public List<Double> getAllTicketTypePrices(){
        return ticketTypeRepository.findAll().stream()
                .map(TicketType::getTypePrice)
                .toList();
    }

    // return a list of all ticket type isActives
    @Override
    public List<Boolean> getAllTicketTypeIsActives(){
        return ticketTypeRepository.findAll().stream()
                .map(TicketType::getIsActive)
                .toList();
    }

    // return a list of all ticket type prices from ticket type isActives
//    @Override
//    public List<Double> getAllTicketTypePricesFromTicketTypeIsActives(){
//        return ticketTypeRepository.findAll().stream()
//                .filter(TicketType::getIsActive)
//                .map(TicketType::getTypePrice)
//                .toList();
//    }
//
//    // return a list of all ticket type names from ticket type isActives
//    @Override
//    public List<String> getAllTicketTypeNamesFromTicketTypeIsActives(){
//        return ticketTypeRepository.findAll().stream()
//                .filter(TicketType::getIsActive)
//                .map(TicketType::getTypeName)
//                .toList();
//    }
//
//    // return a list of all ticket type names and prices from ticket type isActives
//    @Override
//    public List<String> getAllTicketTypeNamesAndPricesFromTicketTypeIsActives(){
//        return ticketTypeRepository.findAll().stream()
//                .filter(TicketType::getIsActive)
//                .map(ticketType -> ticketType.getTypeName() + " - $" + ticketType.getTypePrice())
//                .toList();
//    }


    public List<TicketType> getAllTicketTypesDetails(){
        return ticketTypeRepository.findAll();
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

    // check if TicketType exists by typeName and throw exception if exists
//    @Override
//    public void checkTicketTypeExistsByTypeName(String typeName){
//        for (TicketType existingTicketType : ticketTypeRepository.findAll()) {
//            if (existingTicketType.getTypeName().equals(typeName)) {
//                throw new IllegalStateException("Ticket Type already exists");
//            }
//        }
//        System.out.println("Ticket Type does not exist");
//    }
    // retrieve Ticket_type by UUID

    // find ticketType by typeName
    @Override
    public TicketType getTicketTypeByTypeName(String typeName){
        TicketType ticketType = ticketTypeRepository.findByTypeName(typeName);
        return ticketType;
    }

    //update Ticket_Type by typeName, method will take targetTypeName and update all details about it
    @Override
    public void updateTicketType(String targetTypeName, String newTypeName, Double newTypePrice, Boolean newIsActive){
        //checkTicketTypeExistsByTypeName(targetTypeName);
        TicketType ticketType = ticketTypeRepository.findByTypeName(targetTypeName);
        ticketType.setTypeName(newTypeName);
        ticketType.setTypePrice(newTypePrice);
        ticketType.setIsActive(newIsActive);
        ticketTypeRepository.save(ticketType);
    }

    //update Ticket_Type by typeName, method will take targetTypeName and newTypeName as input
    @Override
    public void updateTicketTypeByTypeName(String targetTypeName, String newTypeName){
        //checkTicketTypeExistsByTypeName(newTypeName);
        TicketType ticketType = ticketTypeRepository.findByTypeName(targetTypeName);
        ticketType.setTypeName(newTypeName);
        ticketTypeRepository.save(ticketType);
    }

    //update Ticket_Type by typePrice, method will take targetTypeName and newTypePrice as input
    @Override
    public void updateTicketTypeByTypePrice(String targetTypeName, Double newTypePrice){
        //checkTicketTypeExistsByTypeName(targetTypeName);
        TicketType ticketType = ticketTypeRepository.findByTypeName(targetTypeName);
        ticketType.setTypePrice(newTypePrice);
        ticketTypeRepository.save(ticketType);
    }

    //update Ticket_Type by isActive, method will take targetTypeName and newIsActive as input
    @Override
    public void updateTicketTypeByIsActive(String targetTypeName, Boolean newIsActive){
        //checkTicketTypeExistsByTypeName(targetTypeName);
        TicketType ticketType = ticketTypeRepository.findByTypeName(targetTypeName);
        ticketType.setIsActive(newIsActive);
        ticketTypeRepository.save(ticketType);
    }

    // update Ticket_Type by all fields, method will take targetTypeName, newTypeName, newTypePrice, newIsActive as input
    @Override
    public void updateTicketTypeByAllFields(String targetTypeName, String newTypeName, Double newTypePrice, Boolean newIsActive){
        //checkTicketTypeExistsByTypeName(targetTypeName);
        TicketType ticketType = ticketTypeRepository.findByTypeName(targetTypeName);
        ticketType.setTypeName(newTypeName);
        ticketType.setTypePrice(newTypePrice);
        ticketType.setIsActive(newIsActive);
        ticketTypeRepository.save(ticketType);
    }
}
