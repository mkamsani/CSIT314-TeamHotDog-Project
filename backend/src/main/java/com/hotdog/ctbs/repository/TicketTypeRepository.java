package com.hotdog.ctbs.repository;

import com.hotdog.ctbs.entity.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketTypeRepository extends JpaRepository<TicketType, UUID> {

    TicketType findByTypeName(String typeName);

    TicketType findTicketTypeByID(UUID id);

    TicketType findTicketTypeByIsActiveAndTypePrice(Boolean isActive);
}