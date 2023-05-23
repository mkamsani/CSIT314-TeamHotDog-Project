package com.hotdog.ctbs.controller.admin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotdog.ctbs.entity.UserAccount;
import com.hotdog.ctbs.repository.UserAccountRepository;
import com.hotdog.ctbs.repository.UserProfileRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    private final UserAccountRepository userAccountRepo;
    private final UserProfileRepository userProfileRepo;
    private final ObjectMapper objectMapper;

    public AdminUserAccountUpdateController(UserAccountRepository userAccountRepo,
                                            UserProfileRepository userProfileRepo)
    {
        this.userAccountRepo = userAccountRepo;
        this.userProfileRepo = userProfileRepo;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    /** Update a {@code UserAccount} based on the given JSON. */
    @PutMapping("/update/{targetUsername}")
    public ResponseEntity<String> Update(@RequestBody String json,
                                         @PathVariable String targetUsername)
    {
        System.out.println("AdminUserAccountUpdateController.Update() called.");
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            UserAccount.updateUserAccount(userAccountRepo,
                                          userProfileRepo,
                                          targetUsername,
                                          jsonNode.get("username").asText(),
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
