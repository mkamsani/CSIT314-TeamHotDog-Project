package com.hotdog.ctbs.controller.admin;

// Application imports.
import com.hotdog.ctbs.service.implementation.UserAccountImpl;

// Java imports.
import java.time.LocalDate;

// JSON deserialization imports.
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// Spring imports.
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The {@code AdminUserAccountUpdateController} class exposes
 * the {@code /api/admin/user-account/update} endpoint.
 * <p />
 *
 * The expected JSON format is:
 * <blockquote><pre>
 * {
 *   "username":    "scarell",
 *   "email":       "scarell@hotdogbuns.com",
 *   "firstName":   "Steve",
 *   "lastName":    "Carell
 *   "dateOfBirth": "1962-08-16",
 *   "address":     "126 Updated Location, Scranton, PA 000000",
 *   "title":       "senior manager"
 * }
 * </pre></blockquote>
 *
 * See {@link AdminUserAccountCreateController#Create(String)} for the suggested HTML form format.
 *
 * @author Baraq Kamsani
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/admin/user-account")
public class AdminUserAccountUpdateController {

    private final UserAccountImpl userAccountImpl;
    private final ObjectMapper objectMapper;

    public AdminUserAccountUpdateController(UserAccountImpl userAccountImpl)
    {
        this.userAccountImpl = userAccountImpl;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    /** Update a {@code UserAccount} based on the given JSON. */
    @PutMapping("/update/{targetUsername}")
    public ResponseEntity<String> Update(@RequestBody String json, @PathVariable String targetUsername)
    {
        System.out.println("AdminUserAccountUpdateController.Update() called.");
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            String username = jsonNode.get("username").asText();
            userAccountImpl.update(
                    targetUsername,
                    username,
                    jsonNode.get("firstName").asText(),
                    jsonNode.get("lastName").asText(),
                    jsonNode.get("email").asText(),
                    jsonNode.get("address").asText(),
                    LocalDate.parse(jsonNode.get("dateOfBirth").asText()),
                    jsonNode.get("title").asText()
            );
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
