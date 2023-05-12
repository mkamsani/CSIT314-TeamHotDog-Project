package com.hotdog.ctbs.entity;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.UUID;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "loyalty_point")
public class LoyaltyPoint {
    @Id
    @Column(name = "uuid", nullable = false)
    private UUID id;

    @MapsId
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "uuid", nullable = false)
    private UserAccount userAccount;

    @Column(name = "points_redeemed", nullable = false)
    private Integer pointsRedeemed;

    @Column(name = "points_total", nullable = false)
    private Integer pointsTotal;

    @SneakyThrows
    @Override
    public String toString(){
        ObjectNode json = new ObjectMapper().createObjectNode();
        json.put("userName", userAccount.getUsername());
        json.put("pointsRedeemed", pointsRedeemed);
        json.put("pointsTotal", pointsTotal);
        return json.toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof LoyaltyPoint that)) return false;
        return id.equals(that.id) && userAccount.equals(that.userAccount);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, userAccount);
    }
}