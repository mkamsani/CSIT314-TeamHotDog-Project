package com.hotdog.ctbs.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotdog.ctbs.repository.LoyaltyPointRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "loyalty_point")
public class LoyaltyPoint {
    @Id
    @Column(name = "uuid", nullable = false)
    protected UUID id;

    @MapsId
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "uuid", nullable = false)
    protected UserAccount userAccount;

    @Column(name = "points_redeemed", nullable = false)
    protected Integer pointsRedeemed;

    @Column(name = "points_total", nullable = false)
    protected Integer pointsTotal;

    @Transient
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    protected Integer pointsBalance()
    {
        return pointsTotal - pointsRedeemed;
    }

    //////////////////////////////// Service /////////////////////////////////

    public static String readLoyaltyPoint(LoyaltyPointRepository loyaltyPointRepo,
                                          String param)
    {
        List<LoyaltyPoint> loyaltyPointList = switch (param) {
            case "all" -> loyaltyPointRepo.findAll();
            case "active" -> loyaltyPointRepo.findAllByUserAccountIsActiveTrue();
            default -> {
                LoyaltyPoint tmp = loyaltyPointRepo.findByUserAccountUsername(param).orElse(null);
                if (tmp == null)
                    throw new IllegalArgumentException("Username does not exist: " + param);
                yield List.of(tmp);
            }
        };

        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (LoyaltyPoint loyaltyPoint : loyaltyPointList) {
            ObjectNode on = objectMapper.createObjectNode();
            on.put("pointsRedeemed", loyaltyPoint.pointsRedeemed);
            on.put("pointsTotal", loyaltyPoint.pointsTotal);
            on.put("username", loyaltyPoint.userAccount.username);
            on.put("pointsBalance", loyaltyPoint.pointsBalance());
            arrayNode.add(on);
        }
        // if size > 1 then [ { ... }, { ... } ] else { ... }
        return arrayNode.size() > 1 ? arrayNode.toString() : arrayNode.get(0).toString();
    }

    public static void redeemLoyaltyPoint(LoyaltyPointRepository loyaltyPointRepo,
                                          String username,
                                          Integer point)
    {
        LoyaltyPoint loyaltyPoint = loyaltyPointRepo.findByUserAccountUsername(username)
                                                    .orElse(null);
        if (loyaltyPoint == null)
            throw new IllegalArgumentException("Username does not exist: " + username);
        loyaltyPoint.pointsRedeemed += point;
        loyaltyPointRepo.save(loyaltyPoint);
    }

}