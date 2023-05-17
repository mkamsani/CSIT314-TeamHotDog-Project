package com.hotdog.ctbs.controller.admin;

// Application imports.
import com.hotdog.ctbs.entity.UserAccount;
import com.hotdog.ctbs.repository.UserAccountRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The {@code AdminUserAccountReadController} class exposes
 * the {@code /api/admin/user-account/read} endpoint.
 * <p />
 *
 * The returned JSON format is:
 * <blockquote><pre>
 * [
 *   {
 *     "username":      "mscott",
 *     "email":         "mscott@hotdogbuns.com",
 *     "firstName":     "Michael",
 *     "lastName":      "Scott",
 *     "dateOfBirth":   "1962-08-16",
 *     "address":       "621 Court Kellum, Not Narcs, AP 01581",
 *     "title":         "senior manager",
 *     "privilege":     "manager",
 *     "isActive":      "true",
 *     "timeCreated":   "2021-04-01T00:00:00.000+00:00",
 *     "timeLastLogin": "2021-04-01T00:00:00.000+00:00",
 *   },
 *   {
 *     "username":      "dwallace",
 *     "email":         "dwallace@hotdogbuns.com",
 *     "firstName":     "David",
 *     "lastName":      "Wallace",
 *     "dateOfBirth":   "1965-02-13",
 *     "address":       "6818 Smith Lake, Schimmelland, RI 93473",
 *     "title":         "chief financial officer",
 *     "privilege":     "owner",
 *     "isActive":      "true",
 *     "timeCreated":   "2021-04-01T00:00:00.000+00:00",
 *     "timeLastLogin": "2021-04-01T00:00:00.000+00:00"
 *   }
 * ]
 * </pre></blockquote>
 *
 * A singular UserAccount object would be wrapped in an array:
 * <blockquote><pre>
 * [
 *   {
 *     "username": "mscott",
 *     ...
 *     "privilege": "manager"
 *   }
 * ]
 * </pre></blockquote>
 *
 * @author Baraq Kamsani
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/admin/user-account")
public class AdminUserAccountReadController {

    private final UserAccountRepository userAccountRepo;

    public AdminUserAccountReadController(final UserAccountRepository userAccountRepo)
    {
        this.userAccountRepo = userAccountRepo;
    }

    /** Read a JSON array of {@code UserAccount} object(s). */
    @GetMapping(value = "/read/{param}")
    public ResponseEntity<String> Read(@PathVariable final String param)
    {
        try {
            String json = UserAccount.readUserAccount(userAccountRepo, param);
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
