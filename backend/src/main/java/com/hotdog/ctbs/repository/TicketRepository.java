package com.hotdog.ctbs.repository;


import com.hotdog.ctbs.entity.Screening;
import com.hotdog.ctbs.entity.Ticket;
import com.hotdog.ctbs.entity.Seat;
import com.hotdog.ctbs.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID>{

    Optional<List<Ticket>> findTicketsByScreening(Screening screening);
    
    Ticket findTicketByScreeningAndSeat(Screening screening, Seat seat);

    List<Ticket> findTicketsByCustomer(UserAccount userAccount);


}
