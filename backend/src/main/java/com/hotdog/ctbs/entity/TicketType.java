package com.hotdog.ctbs.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ticket_type")
public class TicketType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @Column(name = "type_name", length = 7)
    private String typeName;

    @Column(name = "type_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal typePrice;

    @Column(name = "is_active")
    private Boolean isActive;

}