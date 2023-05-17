package com.hotdog.ctbs.controller.admin;

import com.hotdog.ctbs.entity.UserProfile;
import com.hotdog.ctbs.repository.UserProfileRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The {@code AdminUserProfileReadController} class exposes
 * the {@code /api/admin/user-profile/read} endpoint.
 * <p />
 *
 * The returned JSON format is:
 * <blockquote><pre>
 * [
 *   {
 *     "title":     "chief executive officer",
 *     "privilege": "owner"
 *   },
 *   {
 *     "title":     "junior admin",
 *     "privilege": "admin"
 *   }
 * ]
 * </pre></blockquote>
 *
 * @author Baraq Kamsani
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/admin/user-profile")
public class AdminUserProfileReadController {

    private final UserProfileRepository userProfileRepo;

    public AdminUserProfileReadController(UserProfileRepository userProfileRepo)
    {
        this.userProfileRepo = userProfileRepo;
    }

    @GetMapping("/read/{param}")
    public ResponseEntity<String> Read(@PathVariable String param)
    {
        try {
            String json = UserProfile.readUserProfile(userProfileRepo, param);
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
