package com.hotdog.ctbs.repository;

import com.hotdog.ctbs.entity.LoyaltyPoint;
import com.hotdog.ctbs.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LoyaltyPointRepository extends JpaRepository<LoyaltyPoint, UUID>{


    LoyaltyPoint findByUserAccount(UserAccount userAccount);
}
