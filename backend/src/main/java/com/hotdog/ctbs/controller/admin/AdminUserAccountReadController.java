package com.hotdog.ctbs.controller.admin;

// Application imports.

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotdog.ctbs.entity.UserAccount;
import com.hotdog.ctbs.service.implementation.UserAccountImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
