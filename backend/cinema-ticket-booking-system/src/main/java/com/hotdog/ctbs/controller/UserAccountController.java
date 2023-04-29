package com.hotdog.ctbs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.hotdog.ctbs.service.implementation.UserProfileImpl;
import com.hotdog.ctbs.service.implementation.UserAccountImpl;

/**
 * CRUD for user profiles.
 * <br />
 * All methods are PascalCase to denote public API.
 */
@RestController
@RequestMapping("/api/user-profile")
public class UserAccountController {

    private final UserAccountImpl userAccountImpl;

    public UserAccountController(UserAccountImpl userAccountImpl)
    {
        this.userAccountImpl = userAccountImpl;
    }

    /**
     * <pre>
     * wget -qO- --post-data '{"username":"admin","password":"admin"}' --header "Content-Type: application/json" http://localhost:8080/api/user-profile/login
     * curl -X POST -H "Content-Type: application/json" -d '{"username":"admin","password":"admin"}' http://localhost:8080/api/user-profile/login
     * </pre>
     */
    @PostMapping("/login")
    public String Login(@RequestBody String json)
    {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String username = jsonNode.get("username").asText();
            String password = jsonNode.get("password").asText();
            return userAccountImpl.login(username, password);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

}
