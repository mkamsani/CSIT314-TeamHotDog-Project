package com.hotdog.ctbs.controller.admin;

// Application imports.
import com.hotdog.ctbs.repository.UserAccountRepository;
import com.hotdog.ctbs.entity.UserAccount;

// Spring imports.
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The {@code AdminUserAccountSuspendController} class exposes
 * the {@code /api/admin/user-account/suspend} endpoint.
 * <p />
 *
 * The HTML form should GET
 * {@link AdminUserAccountReadController#Read(String) /api/admin/user-account/read/active}
 * to obtain the list of usernames.
 *
 * @author Baraq Kamsani
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/admin/user-account")
public class AdminUserAccountSuspendController {

    private final UserAccountRepository userAccountRepo;

    public AdminUserAccountSuspendController(UserAccountRepository userAccountRepo)
    {
        this.userAccountRepo = userAccountRepo;
    }

    /** Suspend a {@code UserAccount} based on the given {@code PathVariable}. */
    @DeleteMapping("/suspend/{targetUsername}")
    public ResponseEntity<String> Suspend(@PathVariable String targetUsername)
    {
        try {
            UserAccount.suspendUserAccount(userAccountRepo, targetUsername);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
