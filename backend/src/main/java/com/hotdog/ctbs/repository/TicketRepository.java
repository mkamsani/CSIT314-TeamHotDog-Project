package com.hotdog.ctbs.repository;

import com.hotdog.ctbs.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    List<Ticket> findTicketsByCustomer_Username(String username);
}
