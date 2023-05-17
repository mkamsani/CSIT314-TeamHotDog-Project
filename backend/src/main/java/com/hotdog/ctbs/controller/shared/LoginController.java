package com.hotdog.ctbs.controller.shared;

// Application imports.
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotdog.ctbs.entity.UserAccount;

// JSON deserialization imports.
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

// Spring imports.
import com.hotdog.ctbs.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final UserAccountRepository userAccountRepository;
    private final ObjectMapper objectMapper;

    public LoginController(UserAccountRepository userAccountRepository)
    {
        this.userAccountRepository = userAccountRepository;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    // curl -X POST -H "Content-Type: application/json" -d '{"username":"mscott","password":"password-employee"}' http://localhost:8080/api/login
    /** @return the privilege of the user, or an error message */
    @PostMapping("/login")
    String Login(@RequestBody String json)
    {
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            String username = jsonNode.get("username").asText();
            String password = jsonNode.get("password").asText();
            return UserAccount.login(userAccountRepository, username, password);
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
