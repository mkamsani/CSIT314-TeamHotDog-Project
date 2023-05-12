package com.hotdog.ctbs.service.implementation;

import com.hotdog.ctbs.entity.LoyaltyPoint;
import com.hotdog.ctbs.entity.UserAccount;
import com.hotdog.ctbs.repository.*;
import com.hotdog.ctbs.service.LoyaltyPointService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoyaltyPointImpl implements LoyaltyPointService{

    final LoyaltyPointRepository loyaltyPointRepo;
    final UserAccountRepository userAccountRepo;

    public LoyaltyPointImpl(LoyaltyPointRepository loyaltyPointRepo,
                            UserAccountRepository userAccountRepo)
    {
        this.loyaltyPointRepo = loyaltyPointRepo;
        this.userAccountRepo = userAccountRepo;
    }

    // return a list of all loyalty point
    @Override
    public List<LoyaltyPoint> getAllLoyaltyPoint(){
        return loyaltyPointRepo.findAll();
    }

    // get the loyalty point of a user
    public LoyaltyPoint getLoyaltyPointByUser(String username){
        UserAccount userAccount = userAccountRepo.findUserAccountByUsername(username).orElse(null);
        //if null, throw exception
        if(userAccount == null){
            throw new IllegalArgumentException("User does not exist");
        }
        return loyaltyPointRepo.findByUserAccount(userAccount);
    }




}
