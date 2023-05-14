package com.hotdog.ctbs.controller.admin;

// Application imports.
import com.hotdog.ctbs.service.implementation.UserAccountImpl;
import com.hotdog.ctbs.entity.UserAccount;

// Java imports.
import java.util.List;

// JSON serialization imports.
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// Spring imports.
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The {@code UserAccountReadController} class exposes
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
public class UserAccountReadController {

    private final UserAccountImpl userAccountImpl;
    private final ObjectMapper objectMapper;

    public UserAccountReadController(UserAccountImpl userAccountImpl)
    {
        this.userAccountImpl = userAccountImpl;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    /** Read a JSON array of {@code UserAccount} object(s). */
    @GetMapping(value = "/read/{param}")
    public String Read(@PathVariable final String param)
    {
        try {
            List<UserAccount> userAccountList = switch (param) {
                case "admin", "owner", "manager", "customer" ->
                        userAccountImpl.getUserAccountsByPrivilege(param);
                case "active" ->
                        userAccountImpl.getActiveUserAccounts();
                case "all" ->
                        userAccountImpl.getAllUserAccounts();
                default ->
                        List.of(userAccountImpl.getUserAccountByUsername(param));
            };
            JsonNode[] jsonNodes = new JsonNode[userAccountList.size()];
            ArrayNode arrayNode = objectMapper.createArrayNode();
            for (int i = userAccountList.size() - 1; i >= 0; i--) {
                UserAccount userAccount = userAccountList.get(i);
                String title = userAccount.getUserProfile().getTitle();
                String privilege = userAccount.getUserProfile().getPrivilege();
                jsonNodes[i] = objectMapper.valueToTree(userAccount);
                ((ObjectNode) jsonNodes[i]).remove("id");
                ((ObjectNode) jsonNodes[i]).remove("passwordHash");
                ((ObjectNode) jsonNodes[i]).put("title", title);
                ((ObjectNode) jsonNodes[i]).put("privilege", privilege);
                arrayNode.add(jsonNodes[i]);
            }
            return objectMapper.writeValueAsString(arrayNode);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
