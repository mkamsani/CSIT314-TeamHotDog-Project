package com.hotdog.ctbs.controller.customer;

import com.hotdog.ctbs.entity.LoyaltyPoint;
import com.hotdog.ctbs.repository.LoyaltyPointRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/customer/loyalty-point")
public class CustomerLoyaltyPointRedeemController {

    private final LoyaltyPointRepository loyaltyPointRepo;

    public CustomerLoyaltyPointRedeemController(LoyaltyPointRepository loyaltyPointRepo)
    {
        this.loyaltyPointRepo = loyaltyPointRepo;
    }

    @PutMapping("/redeem/{username}/{points}")
    public ResponseEntity<String> Redeem(@PathVariable String username,
                                         @PathVariable int points)
    {
        try {
            LoyaltyPoint.redeemLoyaltyPoint(loyaltyPointRepo, username, points);
            return ResponseEntity.ok().body("Redeemed " + points + " points for " + username);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
