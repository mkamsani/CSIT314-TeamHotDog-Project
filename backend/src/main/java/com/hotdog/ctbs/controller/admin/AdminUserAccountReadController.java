package com.hotdog.ctbs.controller.admin;

// Application imports.
import com.hotdog.ctbs.service.implementation.UserAccountImpl;
import com.hotdog.ctbs.entity.UserAccount;

// Java imports.
import java.util.List;

// JSON serialization imports.
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// Spring imports.
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The {@code AdminUserAccountReadController} class exposes
 * the {@code /api/admin/user-account/read} endpoint.
 * <p />
 *
 * The returned JSON format is:
 * <blockquote><pre>
 * [
 *   {
 *     "username":    "mscott",
 *     "email":       "mscott@hotdogbuns.com",
 *     "firstName":   "Michael",
 *     "lastName":    "Scott",
 *     "dateOfBirth": "1962-08-16",
 *     "address":     "621 Court Kellum, Not Narcs, AP 01581",
 *     "title":       "senior manager",
 *     "privilege":   "manager"
 *   },
 *   {
 *     "username":    "dwallace",
 *     "email":       "dwallace@hotdogbuns.com",
 *     "firstName":   "David",
 *     "lastName":    "Wallace",
 *     "dateOfBirth": "1965-02-13",
 *     "address":     "6818 Smith Lake, Schimmelland, RI 93473",
 *     "title":       "chief financial officer"
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

    private final UserAccountImpl userAccountImpl;
    private final ObjectMapper objectMapper;

    public AdminUserAccountReadController(UserAccountImpl userAccountImpl)
    {
        this.userAccountImpl = userAccountImpl;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    /** Read a JSON array of {@code UserAccount} object(s). */
    @GetMapping(value = "/read/{param}")
    public ResponseEntity<String> Read(@PathVariable final String param)
    {
        try {
            List<UserAccount> uaList = switch (param) {
                case "admin", "owner", "manager", "customer" ->
                        userAccountImpl.getUserAccountsByPrivilege(param);
                case "active" ->
                        userAccountImpl.getActiveUserAccounts();
                case "all" ->
                        userAccountImpl.getAllUserAccounts();
                default ->
                        List.of(userAccountImpl.getUserAccountByUsername(param));
            };
            // Sort list by TimeLastLogin, then Username.
            uaList.sort((ua1, ua2) -> {
                            int timeLastLogin = ua2.getTimeLastLogin().compareTo(ua1.getTimeLastLogin());
                            if (timeLastLogin == 0)
                                return ua1.getUsername().compareTo(ua2.getUsername());
                            return timeLastLogin;
                        }
            );
            ArrayNode an = objectMapper.createArrayNode();
            for (UserAccount ua : uaList) {
                ObjectNode on = objectMapper.createObjectNode();
                on.put("username",      ua.getUsername());
                on.put("email",         ua.getEmail());
                on.put("firstName",     ua.getFirstName());
                on.put("lastName",      ua.getLastName());
                on.put("dateOfBirth",   ua.getDateOfBirth().toString());
                on.put("address",       ua.getAddress());
                on.put("isActive",      ua.getIsActive().toString());
                on.put("timeCreated",   ua.getTimeCreated().toString());
                on.put("timeLastLogin", ua.getTimeLastLogin().toString());
                on.put("title",         ua.getUserProfile().getTitle());
                on.put("privilege",     ua.getUserProfile().getPrivilege());
                an.add(on);
            }
            return ResponseEntity.ok(objectMapper.writeValueAsString(an));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
