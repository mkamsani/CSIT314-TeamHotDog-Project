package com.hotdog.ctbs.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "loyalty_point")
public class LoyaltyPoint {
    private UUID id;

    private UserAccount userAccount;

    private Integer pointsRedeemed;

    private Integer pointsTotal;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", nullable = false)
    public UUID getId()
    {
        return id;
    }

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "uuid", nullable = false)
    public UserAccount getUserAccount()
    {
        return userAccount;
    }

    @Column(name = "points_redeemed", nullable = false)
    public Integer getPointsRedeemed()
    {
        return pointsRedeemed;
    }

    @Column(name = "points_total", nullable = false)
    public Integer getPointsTotal()
    {
        return pointsTotal;
    }

}