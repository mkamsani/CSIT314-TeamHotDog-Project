package com.hotdog.ctbs.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

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
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "customer", nullable = false)
    private UserAccount customer;

    //@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ticket_type", nullable = false, referencedColumnName = "type_name")
    private TicketType ticketType;

    //@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "screening", nullable = false)
    private Screening screening;

    //@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "seat", nullable = false)
    private Seat seat;

    @Column(name = "purchase_date", nullable = false)
    private OffsetDateTime purchaseDate;


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
    @Override
    public String toString()
    {
        ObjectNode json = new ObjectMapper().createObjectNode();
        //json.put("id",       id.toString());
        json.put("customer", customer.getUsername());
        json.put("type",     ticketType.getTypeName());
        json.put("price",    ticketType.getTypePrice());
        // change row to string
        json.put("cinemaRoom",     String.valueOf(seat.getCinemaRoom().getId()));
        json.put("row",      String.valueOf(seat.getSeatRow()));
        json.put("column",   seat.getSeatColumn());
        //json.put("room",     screening.getId().toString());
        json.put("showTime",     screening.getShowTime());
        json.put("showDate",     screening.getShowDate().toString());
        json.put("movie",    screening.getMovie().getTitle());
        json.put("purchaseDate", purchaseDate.toString());
        return json.toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Ticket that)) return false;
        return id.equals(that.id) &&
               screening.equals(that.screening) &&
               seat.equals(that.seat);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, screening, seat);
    }


}