package com.hotdog.ctbs.controller.admin;

// Application imports.
import com.hotdog.ctbs.service.implementation.UserAccountImpl;

// Spring imports.
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The {@code UserAccountSuspendController} class exposes
 * the {@code /api/admin/user-account/suspend} endpoint.
 * <p />
 *
 * The HTML form should GET
 * {@link UserAccountReadController#Read(String) /api/admin/user-account/read/active}
 * to obtain the list of usernames.
 *
 * @author Baraq Kamsani
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/admin/user-account")
public class UserAccountSuspendController {

    private final UserAccountImpl userAccountImpl;

    public UserAccountSuspendController(UserAccountImpl userAccountImpl)
    {
        this.userAccountImpl = userAccountImpl;
    }

    /** Suspend a {@code UserAccount} based on the given {@code PathVariable}. */
    @DeleteMapping("/suspend/{targetUsername}")
    public String Suspend(@PathVariable String targetUsername)
    {
        try {
            userAccountImpl.suspend(targetUsername);
            return "Success";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
