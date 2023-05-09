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

    // curl -X PUT -H "Content-Type: application/json" -d "{\"target\":\"test admin\",\"privilege\":\"manager\",\"title\":\"test manager\"}" http://localhost:8000/api/user-profile/update
    @PutMapping("/update")
    public String Update(@RequestBody String json)
    {
        System.out.println("Method Update() called.");
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String targetTitle = jsonNode.get("target").asText();  // <input name="target" type="text"></input>
            String privilege = jsonNode.get("privilege").asText(); // <input name="privilege" type="text"></input>
            String title = jsonNode.get("title").asText();         // <input name="title" type="text"></input>
            userProfileImpl.updateUserProfileByTitle(targetTitle, privilege, title);
            return "Success";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // curl -X POST -H "Content-Type: application/json" -d "{\"privilege\":\"admin\",\"title\":\"test admin\"}" http://localhost:8000/api/user-profile/create
    @PostMapping("/create")
    public String Create(@RequestBody String json)
    {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String privilege = jsonNode.get("privilege").asText(); // <input name="privilege" type="text"></input>
            String title = jsonNode.get("title").asText();         // <input name="title" type="text"></input>
            userProfileImpl.createUserProfile(privilege, title);
            return "Success";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}