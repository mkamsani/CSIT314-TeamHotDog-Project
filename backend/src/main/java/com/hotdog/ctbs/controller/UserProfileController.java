package com.hotdog.ctbs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import com.hotdog.ctbs.service.implementation.UserProfileImpl;

/**
 * CRUD for user profiles.
 * <br />
 * All methods are PascalCase to denote public API.
 */
@RestController
@RequestMapping("/user-profile")
public class UserProfileController {

    private final UserProfileImpl userProfileImpl;

    public UserProfileController(UserProfileImpl userProfileImpl)
    {
        this.userProfileImpl = userProfileImpl;
    }

    // wget -qO- localhost:8080/user-profile/read/titles
    // curl localhost:8080/user-profile/read/titles
    // curl -X GET localhost:8080/user-profile/read/titles
    @GetMapping("/read/titles")
    public String ReadTitles()
    {
        System.out.println("Method ReadTitles() called.");
        return userProfileImpl.getAllTitles().toString();
    }

    @GetMapping("/read/privileges")
    public String ReadPrivileges()
    {
        System.out.println("Method ReadPrivileges() called.");
        return userProfileImpl.getAllPrivileges().toString();
    }

    @PutMapping("/update")
    public String Update(@RequestBody String json)
    {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String uuid = jsonNode.get("id").asText();
            String privilege = jsonNode.get("privilege").asText();
            String title = jsonNode.get("title").asText();
            userProfileImpl.updateUserProfile(uuid, privilege, title);
            return "Success";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Receives a JSON string of a user profile, this JSON object does not have a UUID.
     * <br />
     * Store the user profile in the database.
     */
    @PostMapping("/create")
    public String Create(@RequestBody String json)
    {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String privilege = jsonNode.get("privilege").asText();
            String title = jsonNode.get("title").asText();
            userProfileImpl.createUserProfile(privilege, title);
            return "Success";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}