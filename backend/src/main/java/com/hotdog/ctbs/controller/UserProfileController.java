package com.hotdog.ctbs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotdog.ctbs.entity.UserProfile;
import org.springframework.web.bind.annotation.*;
import com.hotdog.ctbs.service.implementation.UserProfileImpl;

import java.util.Iterator;
import java.util.List;

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

    // curl -X GET http://localhost:8080/user-profile/read/titles
    @GetMapping("/read/titles")
    public String ReadTitles()
    {
        System.out.println("Method ReadTitles() called.");
        return userProfileImpl.getAllTitles().toString();
    }

    // curl -X GET http://localhost:8000/api/user-profile/read/privileges
    @GetMapping("/read/privileges")
    public String ReadPrivileges()
    {
        System.out.println("Method ReadPrivileges() called.");
        return userProfileImpl.getAllPrivileges().toString();
    }


    /**
     * {@code curl -X GET http://localhost:8000/api/user-profile/read/active-user-profiles}
     * @return [{"privilege":"customer","title":"customer"}, ..., {"privilege":"admin","title":"junior admin"}]
     */
    @GetMapping("/read/active-user-profiles")
    public String ReadActiveUserProfiles()
    {
        System.out.println("Method ReadActiveUserProfiles() called.");
        ObjectMapper objectMapper = new ObjectMapper();
        List<UserProfile> userProfiles = userProfileImpl.getActiveUserProfiles();
        try {
            String json = objectMapper.writeValueAsString(userProfiles);
            JsonNode jsonNode = objectMapper.readTree(json);
            // Remove UUIDs from JSON.
            for (JsonNode node : jsonNode) {
                ((ObjectNode) node).remove("id");
                ((ObjectNode) node).remove("isActive");
            }
            return jsonNode.toString();
        } catch (JsonProcessingException e) {
            return "Error: " + e.getMessage();
        }
    }

    @PutMapping("/update")
    public String Update(@RequestBody String json)
    {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String uuid = jsonNode.get("id").asText();
            String privilege = jsonNode.get("privilege").asText();
            String title = jsonNode.get("title").asText();
            userProfileImpl.updateUserProfileByTitle(uuid, privilege, title);
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