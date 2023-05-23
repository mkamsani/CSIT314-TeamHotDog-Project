package com.hotdog.ctbs.repository;

import com.hotdog.ctbs.entity.LoyaltyPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoyaltyPointRepository extends JpaRepository<LoyaltyPoint, UUID> {

    Optional<LoyaltyPoint> findByUserAccountUsername(String username);

    List<LoyaltyPoint> findAllByUserAccountIsActiveTrue();
}
