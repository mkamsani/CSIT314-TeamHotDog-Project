package com.hotdog.ctbs.repository;

import com.hotdog.ctbs.entity.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TicketTypeRepository extends JpaRepository<TicketType, UUID> {

    Optional<TicketType> findByTypeName(String typeName);

}