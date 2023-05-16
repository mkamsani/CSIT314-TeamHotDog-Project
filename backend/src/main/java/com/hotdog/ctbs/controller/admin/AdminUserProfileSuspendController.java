package com.hotdog.ctbs.controller.admin;

import com.hotdog.ctbs.service.implementation.UserProfileImpl;
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

    private final UserProfileImpl userProfileImpl;

    public AdminUserProfileSuspendController(UserProfileImpl userProfileImpl)
    {
        this.userProfileImpl = userProfileImpl;
    }

    /** Suspend a {@code UserProfile} based on the given {@code PathVariable}. */
    @DeleteMapping("/suspend/{targetTitle}")
    public ResponseEntity<String> Suspend(@PathVariable String targetTitle)
    {
        try {
            userProfileImpl.suspend(targetTitle);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

