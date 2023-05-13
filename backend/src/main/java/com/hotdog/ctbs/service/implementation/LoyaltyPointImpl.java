package com.hotdog.ctbs.service.implementation;

import com.hotdog.ctbs.entity.LoyaltyPoint;
import com.hotdog.ctbs.repository.*;
import com.hotdog.ctbs.service.LoyaltyPointService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoyaltyPointImpl implements LoyaltyPointService {

    final LoyaltyPointRepository loyaltyPointRepo;
    final UserAccountRepository userAccountRepo;

    public LoyaltyPointImpl(LoyaltyPointRepository loyaltyPointRepo,
                            UserAccountRepository userAccountRepo)
    {
        this.loyaltyPointRepo = loyaltyPointRepo;
        this.userAccountRepo = userAccountRepo;
    }

    /** @return the current balance of a {@code LoyaltyPoint} object. */
    @Override
    public Integer getAvailablePoint(LoyaltyPoint loyaltyPoint)
    {
        return loyaltyPoint.getPointsTotal() - loyaltyPoint.getPointsRedeemed();
    }

    /** @return a list of all {@code LoyaltyPoint} objects. */
    @Override
    public List<LoyaltyPoint> getAllLoyaltyPoints()
    {
        return loyaltyPointRepo.findAll();
    }

    /** @return a list of active {@code LoyaltyPoint} objects. */
    @Override
    public List<LoyaltyPoint> getActiveLoyaltyPoints()
    {
        return loyaltyPointRepo.findAll()
                               .stream()
                               .filter(e -> e.getUserAccount().getIsActive())
                               .toList();
    }

    /** @return a {@code LoyaltyPoint} object based on the username of a {@code UserAccount} object. */
    @Override
    public LoyaltyPoint getLoyaltyPointByUsername(String username)
    {
        return loyaltyPointRepo.findByUserAccountUsername(username).orElseThrow(
                () -> new IllegalArgumentException("Username " + username + " does not exist.")
        );
    }

    // TODO :
    //  Should this be called from a TicketController, LoyaltyPointController, or UserAccountController???
    //  Feedbacks welcome, thanks.
    // just some feed back: abit hard to justify where it should fall under
    // if it is other class controller, can it be called? since it is part of loyalty point impl
    // but its seems would be very related to create ticket... pls see ticket impl create ticket method
    // which has include loyaltypoint option
    @Override
    public void redeem(String username, Integer point)
    {
        LoyaltyPoint loyaltyPoint = getLoyaltyPointByUsername(username);
        loyaltyPoint.setPointsRedeemed(loyaltyPoint.getPointsRedeemed() + point);
        loyaltyPointRepo.save(loyaltyPoint);
    }
}
