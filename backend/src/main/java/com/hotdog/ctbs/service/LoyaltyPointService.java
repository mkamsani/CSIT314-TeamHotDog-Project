package com.hotdog.ctbs.service;

import com.hotdog.ctbs.entity.LoyaltyPoint;
import java.util.List;

public interface LoyaltyPointService {

    Integer getAvailablePoint(LoyaltyPoint loyaltyPoint);

    List<LoyaltyPoint> getAllLoyaltyPoints();

    List<LoyaltyPoint> getActiveLoyaltyPoints();

    LoyaltyPoint getLoyaltyPointByUsername(String username);

    void redeem(String username, Integer point);
}
