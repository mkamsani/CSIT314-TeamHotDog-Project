package com.hotdog.ctbs.controller.admin;

// Application imports.
import com.hotdog.ctbs.entity.UserAccount;
import com.hotdog.ctbs.repository.UserAccountRepository;
import com.hotdog.ctbs.repository.UserProfileRepository;

// Java imports.
import java.time.LocalDate;

// JSON deserialization imports.
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// Spring imports.
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The {@code AdminUserAccountCreateController} class exposes
 * the {@code /api/admin/user-account/create} endpoint.
 * <p />
 *
 * The expected JSON format is:
 * <blockquote><pre>
 * {
 *   "username":    "mscott",
 *   "email":       "mscott@hotdogbuns.com",
 *   "password":    "password-employee",
 *   "firstName":   "Michael",
 *   "lastName":    "Scott",
 *   "dateOfBirth": "1962-08-16",
 *   "address":     "621 Court Kellum, Not Narcs, AP 01581",
 *   "title":       "senior manager"
 * }
 * </pre></blockquote>
 *
 * The HTML form should GET {@link AdminUserAccountReadController#Read(String) /api/admin/user-profile/read/titles} to obtain the list of titles.
 * <br />
 * The suggested HTML form format is:
 * <blockquote><pre>
 * &lt;form&gt;
 *   &lt;input type="text" name="username"&gt;
 *   &lt;input type="text" name="email"&gt;
 *   &lt;input type="text" name="password"&gt;
 *   &lt;input type="text" name="firstName"&gt;
 *   &lt;input type="text" name="lastName"&gt;
 *   &lt;input type="text" name="dateOfBirth"&gt;
 *   &lt;input type="text" name="address"&gt;
 *   &lt;select name="title"&gt;
 *     &lt;option value="senior manager"&gt;Senior Manager&lt;/option&gt;
 *     &lt;option value="junior manager"&gt;Manager&lt;/option&gt;
 *     &lt;option value="customer"&gt;Customer&lt;/option&gt;
 *   &lt;/select&gt;
 *   &lt;button type="submit"&gt;Submit&lt;/button&gt;
 * &lt;/form&gt;
 * </pre></blockquote>
 *
 * @author Baraq Kamsani
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/admin/user-account")
public class AdminUserAccountCreateController {

    private final UserAccountRepository userAccountRepo;
    private final UserProfileRepository userProfileRepo;
    private final ObjectMapper objectMapper;

    public AdminUserAccountCreateController(UserAccountRepository userAccountRepo,
                                            UserProfileRepository userProfileRepo)
    {
        this.userAccountRepo = userAccountRepo;
        this.userProfileRepo = userProfileRepo;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    /** Create a {@code UserAccount} based on the given JSON. */
    @PostMapping("/create")
    public ResponseEntity<String> Create(@RequestBody final String json)
    {
        System.out.println("AdminUserAccountCreateController.Create() called.");
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            String username = jsonNode.get("username").asText();
            UserAccount.createUserAccount(
                    userAccountRepo,
                    userProfileRepo,
                    username,
                    jsonNode.get("password").asText(),
                    jsonNode.get("email").asText(),
                    jsonNode.get("firstName").asText(),
                    jsonNode.get("lastName").asText(),
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
