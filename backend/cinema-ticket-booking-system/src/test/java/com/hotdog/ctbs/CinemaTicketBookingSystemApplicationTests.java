package com.hotdog.ctbs;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class CinemaTicketBookingSystemApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testCreateUserProfile() {
        final String url = "http://localhost:8080/api/admin/test/create/fake";
        String result = new RestTemplate().getForObject(url, String.class);
        System.out.println(result);
    }

}
