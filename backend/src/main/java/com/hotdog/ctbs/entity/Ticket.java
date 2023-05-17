package com.hotdog.ctbs.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotdog.ctbs.repository.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    @Column(name = "uuid", nullable = false)
    protected UUID id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "customer", nullable = false)
    protected UserAccount customer;

    //@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ticket_type", nullable = false, referencedColumnName = "type_name")
    protected TicketType ticketType;

    //@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "screening", nullable = false)
    protected Screening screening;

    //@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "seat", nullable = false)
    protected Seat seat;

    @Column(name = "purchase_date", nullable = false)
    protected OffsetDateTime purchaseDate;

    @Transient
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public static void createTicket(UserAccountRepository  userAccountRepository,
                                    TicketTypeRepository   ticketTypeRepository,
                                    ScreeningRepository    screeningRepository,
                                    SeatRepository         seatRepository,
                                    LoyaltyPointRepository loyaltyPointRepository,
                                    CinemaRoomRepository   cinemaRoomRepository,
                                    TicketRepository       ticketRepository,
                                    String                 userName,
                                    String                 ticketTypeName,
                                    String                 showTime,
                                    LocalDate              showDate,
                                    int                    cinemaRoomId,
                                    String                 row,
                                    int                    column,
                                    Boolean                isLoyaltyPointUsed)
    {
        UserAccount userAccount = userAccountRepository.findUserAccountByUsername(userName).orElse(null);
        if (userAccount == null)
            throw new IllegalArgumentException("Username is invalid");

        TicketType ticketType = ticketTypeRepository.findByTypeName(ticketTypeName).orElse(null);
        if (ticketType == null)
            throw new IllegalArgumentException("TicketType is invalid");

        CinemaRoom cinemaRoom = cinemaRoomRepository.findById(cinemaRoomId).orElse(null);
        if (cinemaRoom == null){
            throw new IllegalArgumentException("CinemaRoomID is invalid");
        }

        Screening screening = screeningRepository.findScreeningByShowTimeAndAndShowDateAndAndCinemaRoom(showTime, showDate, cinemaRoomId).orElse(null);
        if (screening == null) {
            throw new IllegalArgumentException("Screening is invalid");
        }

        Seat seat = seatRepository.findSeatBySeatRowAndAndSeatColumnAndCinemaRoom(row.charAt(0), column, cinemaRoom);
        if (seat == null)
            throw new IllegalArgumentException("Seat is invalid");

        // Earmarked to move to CustomerLoyaltyPointUpdate Controller
        LoyaltyPoint loyaltyPointForUser = loyaltyPointRepository.findByUserAccountUsername(userName).orElse(null);
        if (loyaltyPointForUser == null)
            throw new IllegalArgumentException("User does not have any loyalty point");

        if (isLoyaltyPointUsed) {
            Double pointsBalance = Double.valueOf(loyaltyPointForUser.pointsBalance());
            if (pointsBalance < ticketTypeRepository.findByTypeName("redemption").get().typePrice)
                throw new IllegalArgumentException("Loyalty point is not enough");
        }
        else {
            loyaltyPointForUser.setPointsTotal(loyaltyPointForUser.getPointsTotal() + 1);
        }

        Ticket ticket = new Ticket();
        ticket.id = UUID.randomUUID();
        ticket.customer = userAccount;
        ticket.ticketType = ticketType;
        ticket.screening = screening;
        ticket.seat = seat;
        ticket.purchaseDate = OffsetDateTime.now();
        ticketRepository.save(ticket);
    }

    /**
     * Returns a JSON representation of the ticket.
     * <br />
     * Primary keys of references are replaced with their actual superkeys (see UNIQUE constraints).
     *
     * @return <pre>
     * {<br />
     *     "id":         "00000000-0000-0000-0000-000000000000",<br />
     *     "customer":   "username",<br />
     *     "type":       "adult",<br />
     *     "seatRow":    "G",<br />
     *     "seatColumn": "16",<br />
     *     "room":       "1",<br />
     *     "time":       "morning",<br />
     *     "date":       "2021-01-01",<br />
     *     "movie":      "Wonder Woman" <br />
     * }</pre>
     */
    public static String readTicket(UserAccountRepository userAccountRepository,
                                    TicketRepository ticketRepository,
                                    String username)
    {
        UserAccount userAccount = userAccountRepository.findUserAccountByUsername(username).orElse(null);
        if (userAccount == null)
            throw new IllegalArgumentException("Username is invalid");

        List<Ticket> tickets = ticketRepository.findTicketsByCustomer_Username(username);
        ArrayNode an = objectMapper.createArrayNode();
        for (Ticket ticket : tickets) {
            ObjectNode on = objectMapper.createObjectNode();
            on.put("type", ticket.ticketType.typeName);
            on.put("price", ticket.ticketType.typePrice);
            on.put("movie", ticket.screening.movie.title);
            on.put("cinemaRoom", String.valueOf(ticket.seat.cinemaRoom.id));
            on.put("row", String.valueOf(ticket.seat.seatRow));
            on.put("column", ticket.seat.seatColumn);
            on.put("showTime", ticket.screening.showTime);
            on.put("showDate", ticket.screening.showDate.toString());
            on.put("purchaseDate", ticket.purchaseDate.toString());
            an.add(on);
        }
        return an.toString();
    }
}