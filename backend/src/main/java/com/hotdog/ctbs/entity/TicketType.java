package com.hotdog.ctbs.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.math.BigDecimal;
import java.util.Objects;
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
    private Double typePrice;

    @Column(name = "is_active")
    private Boolean isActive;

    @SneakyThrows
    @Override
    public String toString()
    {
        return new ObjectMapper().registerModule(new JavaTimeModule())
                .writeValueAsString(this);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof TicketType that)) return false;
        return uuid.equals(that.uuid) && typeName.equals(that.typeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, typeName, typePrice, isActive);
    }
}