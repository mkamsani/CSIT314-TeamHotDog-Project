package com.hotdog.ctbs.controller.shared;

// Application imports.
import com.hotdog.ctbs.service.implementation.UserAccountImpl;

// JSON deserialization imports.
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

// Spring imports.
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    private final UserAccountImpl userAccountImpl;

    public LoginController(UserAccountImpl userAccountImpl)
    {
        this.userAccountImpl = userAccountImpl;
    }

    /** @return the privilege of the user, or an error message */
    @PostMapping("/login")
    public String Login(@RequestBody String json)
    {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String username = jsonNode.get("userId").asText();
            String password = jsonNode.get("password").asText();
            return userAccountImpl.login(username, password);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
