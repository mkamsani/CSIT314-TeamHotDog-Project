package com.hotdog.ctbs.repository;

import com.hotdog.ctbs.entity.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketTypeRepository extends JpaRepository<TicketType, UUID> {

    Optional<TicketType> findByTypeName(String typeName);

    List<TicketType> findAllByIsActiveTrue();

    default List<String> findAllTypeName() {
        return findAll().stream().map(TicketType::getTypeName).toList();
    }
}