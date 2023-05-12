package com.hotdog.ctbs.implementation;


import com.hotdog.ctbs.entity.*;
import com.hotdog.ctbs.repository.*;
import com.hotdog.ctbs.service.ScreeningService;
import com.hotdog.ctbs.service.implementation.LoyaltyPointImpl;
import com.hotdog.ctbs.service.implementation.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class LoyaltyPointTests {

    @Autowired
    private LoyaltyPointImpl loyaltyPointImpl;

    @Test
    void allMethod(){
        // test getAllLoyaltyPoint()
        System.out.println(loyaltyPointImpl.getAllLoyaltyPoint());
        System.out.println();

        // test getLoyaltyPointByUser()
        System.out.println(loyaltyPointImpl.getLoyaltyPointByUser("user_41"));



    }


}
