package com.hotdog.ctbs.controller.shared;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotdog.ctbs.entity.UserAccount;
import com.hotdog.ctbs.repository.UserAccountRepository;
import com.hotdog.ctbs.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hotdog.ctbs.service.implementation.UserAccountImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/")
public class LoginController {

    final UserAccountImpl userAccountImpl;

    public LoginController(UserAccountImpl userAccountImpl)
    {
        this.userAccountImpl = userAccountImpl;
    }

    /**
     * wget -qO- --header="Content-Type: application/json" --post-data='{"userId":"mscott","password":"password-employee"}' http://localhost:8000/api/login
     * curl -X POST -H "Content-Type: application/json" -d '{"userId":"user_0","password":"password_0"}' http://localhost:8000/api/login
     *
     * @param json '{"userId":"mscott","password":"password-employee"}'
     */
    @PostMapping("/login") // http://localhost:8000/api/login
    public String Login(@RequestBody String json)
    {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String username = jsonNode.get("userId").asText();
            System.out.println("username: " + username);
            String password = jsonNode.get("password").asText();
            System.out.println("password: " + password);
            return userAccountImpl.login(username, password);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
