package com.hotdog.ctbs;

import com.hotdog.ctbs.controller.shared.LoginController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests for LoginController, done as part of CI/CD component of the project.
 */
@WebMvcTest(controllers = LoginController.class)
class CinemaTicketBookingSystemApplicationTests {

    /**
     * Check if the context loads.
     */
    @Test
    void contextLoads()
    {
    }

    @Autowired
    MockMvc mockMvc;

    /**
     * Perform a POST request to the controller, and expect "admin" as the response
     * The JSON passed in is {"username": "samy", "password": "password-employee"}
     * Throw an exception if the returned String is not "admin"
     * The given URL in the LoginController class is @PostMapping("/login")
     */
    @Test
    void checkForLoginController() throws Exception
    {
        String json = """
                {
                    "username": "samy",
                    "password": "password-employee"
                }
                """;
        MockHttpServletRequestBuilder request = post("/login")
                .contentType("application/json")
                .content(json);
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("admin"));
        if (!mockMvc.perform(request).andReturn().getResponse().getContentAsString().equals("admin"))
            throw new Exception("LoginController is not working properly");
        else
            System.out.println("LoginController is working properly");
    }
}
