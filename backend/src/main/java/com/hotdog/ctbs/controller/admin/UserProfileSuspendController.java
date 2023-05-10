package com.hotdog.ctbs.controller.admin;

import com.hotdog.ctbs.service.implementation.UserProfileImpl;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * The {@code UserProfileSuspendController} class exposes
 * the {@code /api/admin/user-profile/suspend} endpoint.
 * <p />
 *
 * The HTML form should GET
 * {@link UserProfileReadController#Read(String) /api/admin/user-profile/read/titles}
 * to obtain the list of titles.
 *
 * @author Baraq Kamsani
 */
public class UserProfileSuspendController {

    private final UserProfileImpl userProfileImpl;

    public UserProfileSuspendController(UserProfileImpl userProfileImpl)
    {
        this.userProfileImpl = userProfileImpl;
    }

    /** Suspend a {@code UserProfile} based on the given {@code PathVariable}. */
    @DeleteMapping("/suspend/{targetTitle}")
    public String Suspend(@PathVariable String targetTitle)
    {
        try {
            userProfileImpl.suspend(targetTitle);
            return "Success";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}

