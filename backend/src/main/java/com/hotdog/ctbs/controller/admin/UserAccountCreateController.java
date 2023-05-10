package com.hotdog.ctbs.controller.admin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotdog.ctbs.service.implementation.UserAccountImpl;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/admin")
public class UserAccountCreateController {

    final UserAccountImpl userAccountImpl;

    public UserAccountCreateController(UserAccountImpl userAccountImpl)
    {
        this.userAccountImpl = userAccountImpl;
    }

    // RequestBody: {"username":"a","email":"a@a.com","password":"a","firstName":"fn1","lastName":"ln1","dateOfBirth":"2023-05-03","address":"address1","title":"customer"}
    @PostMapping("/create")
    public String Create(@RequestBody String json)
    {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String username = jsonNode.get("username").asText();
            String email = jsonNode.get("email").asText();
            String password = jsonNode.get("password").asText();
            String firstName = jsonNode.get("firstName").asText();
            String lastName = jsonNode.get("lastName").asText();
            String address = jsonNode.get("address").asText();
            LocalDate dateOfBirth = LocalDate.parse(jsonNode.get("dateOfBirth").asText());
            String title = jsonNode.get("title").asText();
            userAccountImpl.createUserAccount(username, password, email, firstName, lastName, address, dateOfBirth, title);
            System.out.println("User account " + username + " created successfully.");
            return "User account " + username + " created successfully.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

}
