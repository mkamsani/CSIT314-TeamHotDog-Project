package com.hotdog.ctbs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotdog.ctbs.entity.UserAccount;
import com.hotdog.ctbs.repository.UserAccountRepository;
import com.hotdog.ctbs.repository.UserProfileRepository;
import jakarta.servlet.http.HttpServletRequest;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import com.hotdog.ctbs.service.implementation.UserProfileImpl;
import com.hotdog.ctbs.service.implementation.UserAccountImpl;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * CRUD for user profiles.
 * <br />
 * All methods are PascalCase to denote public API.
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/user-account")
public class UserAccountController {

    private final UserAccountImpl userAccountImpl;
    private final UserProfileRepository userProfileRepository;
    private final UserAccountRepository userAccountRepository;

    public UserAccountController(UserAccountImpl userAccountImpl,
                                 UserProfileRepository userProfileRepository, UserAccountRepository userAccountRepository)
    {
        this.userAccountImpl = userAccountImpl;
        this.userProfileRepository = userProfileRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @PostMapping("/register")
    public String Register(@RequestBody String json)
    {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String username = jsonNode.get("username").asText();
            String password = jsonNode.get("password").asText();
            return userAccountImpl.login(username, password);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * <pre>
     * wget -qO- --post-data '{"username":"admin","password":"admin"}' --header "Content-Type: application/json" http://localhost:8080/api/user-profile/login
     * curl -X POST -H "Content-Type: application/json" -d '{"username":"admin","password":"admin"}' http://localhost:8080/api/user-profile/login
     * </pre>
     */
    @PostMapping("/login")
    public String Login(@RequestBody String json)
    {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String username = jsonNode.get("username").asText();
            String password = jsonNode.get("password").asText();
            return userAccountImpl.login(username, password);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    //
    @GetMapping("/readAllUserAccounts")
    public String readAllUserAccounts()
    {
        return userAccountImpl.getManagerUserAccounts().toString();
    }

    //
    @PostMapping("/createUserAccount")
    public String createUserAccount(@RequestBody String json)
    {
        System.out.println("Method called." + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        System.out.println();
        System.out.println("JSON Data:");
        System.out.println(json);
        System.out.println();
        // Convert %22%7B%22username%22%3A%22testingusername%22%2C%22password%22%3A%22testingpassword%22%2C%22privilege%22%3A%22testingprivilege%22%7D%22= to {"username":"testingusername","password":"testingpassword","privilege":"testingprivilege"}
        json = json.replace("%22%7B%22", "{");
        json = json.replace("%22%3A%22", ":");
        json = json.replace("%22%2C%22", ",");
        json = json.replace("%22%7D%22", "}");
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String username = jsonNode.get("username").asText();
            String password = jsonNode.get("password").asText();
            String privilege = jsonNode.get("privilege").asText();
            System.out.println();
            System.out.println("Username: " + username);
            System.out.println();
            System.out.println("Password: " + password);
            System.out.println();
            System.out.println("Privilege: " + privilege);
            System.out.println();
            Faker faker = new Faker(); // todo remove. for testing
            UserAccount userAccount = UserAccount.builder()
                                                 .passwordHash(password)
                                                 .username(username)
                                                 .email(faker.internet().emailAddress())
                                                 .firstName(username)
                                                 .lastName(username)
                                                 .address(username)
                                                 .dateOfBirth(LocalDate.parse("1990-01-01"))
                                                 .userProfile(userProfileRepository.findUserProfileByTitle("customer"))
                                                 .timeCreated(OffsetDateTime.now())
                                                 .timeLastLogin(OffsetDateTime.now())
                                                 .isActive(true)
                                                 .build();
            int size = userAccountRepository.findAll().size();
            userAccountRepository.save(userAccount);
            if (userAccountRepository.findAll().size() == size + 1) {
                System.out.println("User account " + username + " created successfully");
                return "User account " + username + " created successfully";
            } else {
                System.out.println("User account " + username + " created successfully");
                return "User account " + username + " failed to create";
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }



    // Return HTTP Response object with the user's profile information
    // import org.springframework.http.ResponseEntity;
    @PostMapping("/loginOther")
    public ResponseEntity<String> LoginOther(@RequestBody String json)
    {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String username = jsonNode.get("username").asText();
            String password = jsonNode.get("password").asText();
            return ResponseEntity.ok().body(userAccountImpl.login(username, password));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/immediateSuccess")
    public String success(@RequestBody String json)
    {
        return "http://localhost/index.php";
    }

    @PostMapping("/testResponseEntity")
    public ResponseEntity<String> testResponseEntity(@RequestBody String json, HttpServletRequest request)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", request.getHeader("Origin"));
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String username = jsonNode.get("username").asText();
            String password = jsonNode.get("password").asText();
            return ResponseEntity.ok().headers(headers).body("correct json");
        } catch (Exception e) {
            return ResponseEntity.ok().headers(headers).body("incorrect json");
        }
    }
}
