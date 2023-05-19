package com.hotdog.ctbs.controller.customer;

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
 * The {@code CustomerAccountCreateController} class exposes
 * the {@code /api/customer/user-account/create} endpoint.
 * <p />
 *
 * The expected JSON format is:
 * <blockquote><pre>
 * {
 *   "username":    "sample",
 *   "email":       "sample@example.com",
 *   "password":    "whatever",
 *   "firstName":   "John",
 *   "lastName":    "Doe",
 *   "dateOfBirth": "1999-12-21",
 *   "address":     "123 Courts, Singapore, 5138008",
 * }
 * </pre></blockquote>
 *
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
 *   &lt;button type="submit"&gt;Submit&lt;/button&gt;
 * &lt;/form&gt;
 * </pre></blockquote>
 *
 * @author Baraq Kamsani
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/customer/user-account")
public class CustomerAccountCreateController {

    private final UserAccountRepository userAccountRepo;
    private final UserProfileRepository userProfileRepo;
    private final ObjectMapper objectMapper;

    public CustomerAccountCreateController(UserAccountRepository userAccountRepo,
                                           UserProfileRepository userProfileRepo)
    {
        this.userAccountRepo = userAccountRepo;
        this.userProfileRepo = userProfileRepo;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    /** Create a customer {@code UserAccount} based on the given JSON. */
    @PostMapping("/create")
    public ResponseEntity<String> CreateCustomer(@RequestBody String json)
    {
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
                    "customer"
            );
            return ResponseEntity.ok().body("Success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
