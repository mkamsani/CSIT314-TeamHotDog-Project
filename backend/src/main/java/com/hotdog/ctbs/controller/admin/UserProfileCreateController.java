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
 * The {@code UserProfileCreateController} class exposes
 * the {@code /api/admin/user-profile/create} endpoint.
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
public class UserProfileCreateController {

    private final UserProfileImpl userProfileImpl;
    private final ObjectMapper objectMapper;

    public UserProfileCreateController(UserProfileImpl userProfileImpl)
    {
        this.userProfileImpl = userProfileImpl;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    /** Create a {@code UserProfile} based on the given JSON. */
    @PostMapping("/create")
    public String Create(@RequestBody String json)
    {
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            userProfileImpl.create(
                    jsonNode.get("privilege").asText(),
                    jsonNode.get("title").asText()
            );
            return "Success";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
