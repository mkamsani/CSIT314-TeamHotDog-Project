package com.hotdog.ctbs.service;


import com.hotdog.ctbs.entity.LoyaltyPoint;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface LoyaltyPointService {
    // return a list of all loyalty point
    List<LoyaltyPoint> getAllLoyaltyPoint();
}
