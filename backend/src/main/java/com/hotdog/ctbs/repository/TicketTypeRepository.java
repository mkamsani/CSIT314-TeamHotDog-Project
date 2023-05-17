package com.hotdog.ctbs.repository;

import com.hotdog.ctbs.entity.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketTypeRepository extends JpaRepository<TicketType, UUID> {

    Optional<TicketType> findByTypeName(String typeName);

    List<TicketType> findAllByIsActiveTrue();

    @Query(value = "SELECT type_name FROM ticket_type\n", nativeQuery = true)
    List<String> findAllTypeName();
}