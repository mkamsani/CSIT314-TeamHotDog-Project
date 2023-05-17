package com.hotdog.ctbs.controller.admin;

// Application imports.
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotdog.ctbs.entity.UserProfile;
import com.hotdog.ctbs.repository.UserAccountRepository;
import com.hotdog.ctbs.repository.UserProfileRepository;
import com.hotdog.ctbs.service.implementation.UserProfileImpl;

// JSON deserialization imports.
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// Spring imports.
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    private final UserAccountRepository userAccountRepo;
    private final UserProfileRepository userProfileRepo;
    private final ObjectMapper objectMapper;

    public AdminUserProfileReadController(UserProfileImpl userProfileImpl)
    {
        this.userProfileImpl = userProfileImpl;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @GetMapping("/read/{param}")
    public ResponseEntity<String> Read(@PathVariable String param)
    {
        try {
            if (param.equals("titles"))
                return ResponseEntity.ok(userProfileImpl.getAllTitles().toString());
            if (param.equals("privileges"))
                return ResponseEntity.ok(userProfileImpl.getAllPrivileges().toString());
            List<UserProfile> userProfileList = switch (param) {
                case "admin", "owner", "manager", "customer"
                        -> userProfileImpl.getUserProfilesByPrivilege(param);
                case "active"
                        -> userProfileImpl.getActiveUserProfiles();
                case "all"
                        -> userProfileImpl.getAllUserProfiles();
                default -> List.of(userProfileImpl.getUserProfileByTitle(param));
            };
            JsonNode[] jsonNodes = new JsonNode[userProfileList.size()];
            ArrayNode arrayNode = objectMapper.createArrayNode();
            for (int i = userProfileList.size() - 1; i >= 0; i--) {
                UserProfile userAccount = userProfileList.get(i);
                jsonNodes[i] = objectMapper.valueToTree(userAccount);
                ((ObjectNode) jsonNodes[i]).remove("id");
                arrayNode.add(jsonNodes[i]);
            }
            return ResponseEntity.ok(objectMapper.writeValueAsString(arrayNode));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
