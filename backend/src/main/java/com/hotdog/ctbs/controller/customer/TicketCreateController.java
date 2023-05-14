package com.hotdog.ctbs.controller.customer;

import com.hotdog.ctbs.service.implementation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/user-profile/create")
public class TicketCreateController {

    MovieImpl movieImpl;
    ScreeningImpl screeningImpl;
    TicketTypeImpl ticketTypeImpl;
    UserAccountImpl userAccountImpl;
    TicketImpl ticketImpl;
    // LoyaltyPointImpl loyaltyPointImpl;
}
