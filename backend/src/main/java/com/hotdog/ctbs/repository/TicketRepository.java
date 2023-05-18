package com.hotdog.ctbs.repository;

import com.hotdog.ctbs.entity.Screening;
import com.hotdog.ctbs.entity.Seat;
import com.hotdog.ctbs.entity.Ticket;
import com.hotdog.ctbs.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    @Query("""
            select t from Ticket t
            where t.screening.cinemaRoom.id = ?1
            and   t.screening.showTime = ?2
            and   t.screening.showDate = ?3"""
    )
    Optional<List<Ticket>> findTicketsByScreeningSuperkey(Integer screening_cinemaRoom_id,
                                                          String screening_showTime,
                                                          LocalDate screening_showDate);

    Ticket findTicketByScreeningAndSeat(Screening screening, Seat seat);

    List<Ticket> findTicketsByCustomer(UserAccount userAccount);

    List<Ticket> findTicketsByCustomer_Username(String username);

/*
    List<Ticket> findTicketsByPurchaseDateBetween(LocalDate startDate, LocalDate endDate);
    Optional<List<Ticket>> findTicketsByPurchaseDate(LocalDate date);
    Optional<List<Ticket>> findTicketsByPurchaseDateMonth(Integer month);
*/

    // TODO : Remove and replace with native Java approach.
    @Query(value = """
            SELECT json_agg(json_build_object('seatRow', seat_row, 'seatColumn', seat_column, 'seatConcat', concat(seat_row, seat_column))) "seats"
            FROM (SELECT seat_row, seat_column
                  FROM seat
                  INNER JOIN screening s ON s.cinema_room = seat.cinema_room
                  WHERE s.show_date = ?1
                    AND s.cinema_room = ?2
                    AND s.show_time = ?3
                    AND seat.uuid NOT IN
                        (SELECT ticket.seat
                         FROM ticket
                         INNER JOIN screening ON screening.uuid = ticket.screening
                         WHERE screening.uuid = s.uuid)
                  ORDER BY seat_row, seat_column) AS seats
                  """, nativeQuery = true
    ) // LocalDate showDate, Integer cinemaRoomId, String showTime, returns an ArrayList of Strings
    Object findAvailableSeats(LocalDate showDate, Integer cinemaRoomId, String showTime);
}
