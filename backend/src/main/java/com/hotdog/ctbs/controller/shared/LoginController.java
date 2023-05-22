package com.hotdog.ctbs.controller.shared;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotdog.ctbs.entity.UserAccount;
import com.hotdog.ctbs.repository.UserAccountRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The {@code LoginController} class exposes
 * the {@code /api/login} endpoint.
 * <p />
 *
 * The expected JSON format is:
 * <blockquote><pre>
 * {
 *   "username":    "mscott",
 *   "password":    "password-employee",
 * }
 * </pre></blockquote>
 *
 * @author Baraq Kamsani
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/")
public class LoginController {

    private final UserAccountRepository userAccountRepository;
    private final ObjectMapper objectMapper;

    public LoginController(UserAccountRepository userAccountRepository)
    {
        this.userAccountRepository = userAccountRepository;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    /** @return the privilege of the user, or an error message */
    @PostMapping("/login")
    ResponseEntity<String> Login(@RequestBody String json)
    {
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            String username = jsonNode.get("username").asText();
            String password = jsonNode.get("password").asText();
            String response = UserAccount.validateLogin(userAccountRepository, username, password);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
