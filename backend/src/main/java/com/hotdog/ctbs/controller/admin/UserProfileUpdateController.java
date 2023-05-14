package com.hotdog.ctbs.controller.admin;

// Application imports.
import com.hotdog.ctbs.service.implementation.UserProfileImpl;

// JSON deserialization imports.
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// Spring imports.
import org.springframework.web.bind.annotation.*;

/**
 * The {@code UserProfileUpdateController} class exposes
 * the {@code /api/admin/user-profile/update} endpoint.
 * <p />
 *
 * The expected JSON format is:
 * <blockquote><pre>
 * {
 *   "title":     "chief executive officer",
 *   "privilege": "owner"
 * }
 * </pre></blockquote>
 *
 * @author Baraq Kamsani
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/admin/user-profile")
public class UserProfileUpdateController {

    private final UserProfileImpl userProfileImpl;
    private final ObjectMapper objectMapper;

    public UserProfileUpdateController(UserProfileImpl userProfileImpl)
    {
        this.userProfileImpl = userProfileImpl;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    /** Update a {@code UserProfile} based on the given JSON. */
    @PutMapping("/update/{targetTitle}")
    public String Update(@RequestBody String json, @PathVariable String targetTitle)
    {
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            userProfileImpl.update(
                    targetTitle,
                    jsonNode.get("privilege").asText(),
                    jsonNode.get("title").asText()
            );
            return "Success";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
