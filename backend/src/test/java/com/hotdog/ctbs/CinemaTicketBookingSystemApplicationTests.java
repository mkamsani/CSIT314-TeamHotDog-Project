package com.hotdog.ctbs;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for LoginController, done as part of CI/CD component of the project.
 * Perform a POST request to the controller, and expect "admin", "owner", "manager", or "customer" as a response.
 * The JSON passed are the username and password.
 * Throw an exception if the returned String does not match the expected String.
 * The given URL in the LoginController class is @PostMapping("/login")
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class CinemaTicketBookingSystemApplicationTests {

    @Autowired
    MockMvc mockMvc;

    /** Check if the context loads. */
    @Test
    void contextLoads()
    {
    }

    /** @throws Exception if the status is not 200 or the content is not "admin" */
    @Test
    void TestLoginControllerForAdmin() throws Exception
    {
        MockHttpServletRequestBuilder request = post("/login")
                .contentType("application/json")
                .content("{\"username\":\"samy\",\"password\":\"password-employee\"}\n");
        mockMvc.perform(request)
               .andDo(print())
               // throw an exception if the status is not 200
               .andExpect(status().isOk())
               // throws an exception if the content is not "admin"
               .andExpect(content().string("admin"));
    }

    /** @throws Exception if the status is not 200 or the content is not "owner" */
    @Test
    void TestLoginControllerForOwner() throws Exception
    {
        MockHttpServletRequestBuilder request = post("/login")
                .contentType("application/json")
                .content("{\"username\":\"dwallace\",\"password\":\"password-employee\"}\n");
        mockMvc.perform(request)
               .andDo(print())
               // throw an exception if the status is not 200
               .andExpect(status().isOk())
               // throws an exception if the content is not "owner"
               .andExpect(content().string("owner"));
    }

    /** @throws Exception if the status is not 200 or the content is not "manager" */
    @Test
    void TestLoginControllerForManager() throws Exception
    {
        MockHttpServletRequestBuilder request = post("/login")
                .contentType("application/json")
                .content("{\"username\":\"mscott\",\"password\":\"password-employee\"}\n");
        mockMvc.perform(request)
               .andDo(print())
               // throw an exception if the status is not 200
               .andExpect(status().isOk())
               // throws an exception if the content is not "manager"
               .andExpect(content().string("manager"));
    }

    /** @throws Exception if the status is not 200 or the content is not "customer" */
    @Test
    void TestLoginControllerForCustomer() throws Exception
    {
        MockHttpServletRequestBuilder request = post("/login")
                .contentType("application/json")
                .content("{\"username\":\"customer0\",\"password\":\"password-customer\"}\n");
        mockMvc.perform(request)
               .andDo(print())
               // throw an exception if the status is not 200
               .andExpect(status().isOk())
               // throws an exception if the content is not "customer"
               .andExpect(content().string("customer"));
    }

    @Test
    void TestLoginControllerForInvalid() throws Exception
    {
        MockHttpServletRequestBuilder request = post("/login")
                .contentType("application/json")
                .content("{\"username\":\"customer0\",\"password\":\"wrong-password\"}\n");
        mockMvc.perform(request)
               .andDo(print())
               // throw an exception if the status is not 400
               .andExpect(status().isBadRequest())
               // throws an exception if the content is not "Invalid username or password"
               .andExpect(content().string("Invalid username or password."));
    }
}
