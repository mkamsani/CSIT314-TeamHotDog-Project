package com.hotdog.ctbs.controller.admin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotdog.ctbs.entity.UserProfile;
import com.hotdog.ctbs.repository.UserProfileRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The {@code AdminUserProfileCreateController} class exposes
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
public class AdminUserProfileCreateController {

    private final UserProfileRepository userProfileRepo;
    private final ObjectMapper objectMapper;

    public AdminUserProfileCreateController(UserProfileRepository userProfileRepo)
    {
        this.userProfileRepo = userProfileRepo;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    /** Create a {@code UserProfile} based on the given JSON. */
    @PostMapping("/create")
    public ResponseEntity<String> Create(@RequestBody String json)
    {
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            UserProfile.createUserProfile(
                    userProfileRepo,
                    jsonNode.get("privilege").asText(),
                    jsonNode.get("title").asText()
            );
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
