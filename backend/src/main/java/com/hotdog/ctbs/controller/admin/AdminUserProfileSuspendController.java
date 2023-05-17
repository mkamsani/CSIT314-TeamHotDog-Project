package com.hotdog.ctbs.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotdog.ctbs.entity.UserProfile;
import com.hotdog.ctbs.repository.UserProfileRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The {@code AdminUserProfileSuspendController} class exposes
 * the {@code /api/admin/user-profile/suspend} endpoint.
 * <p />
 *
 * The HTML form should GET
 * {@link AdminUserProfileReadController#Read(String) /api/admin/user-profile/read/titles}
 * to obtain the list of titles.
 *
 * @author Baraq Kamsani
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/admin/user-profile")
public class AdminUserProfileSuspendController {

    private final UserProfileRepository userProfileRepo;
    private final ObjectMapper objectMapper;

    public AdminUserProfileSuspendController(UserProfileRepository userProfileRepo)
    {
        this.userProfileRepo = userProfileRepo;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    /** Suspend a {@code UserProfile} based on the given {@code PathVariable}. */
    @DeleteMapping("/suspend/{targetTitle}")
    public ResponseEntity<String> Suspend(@PathVariable String targetTitle)
    {
        try {
            UserProfile.suspendUserProfile(userProfileRepo, targetTitle);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

