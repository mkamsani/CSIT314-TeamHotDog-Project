package com.hotdog.ctbs.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotdog.ctbs.entity.UserAccount;
import com.hotdog.ctbs.repository.UserAccountRepository;
import com.hotdog.ctbs.repository.UserProfileRepository;
import org.springframework.web.bind.annotation.*;
import com.hotdog.ctbs.service.implementation.UserAccountImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
                                 UserProfileRepository userProfileRepository,
                                 UserAccountRepository userAccountRepository)
    {
        this.userAccountImpl = userAccountImpl;
        this.userProfileRepository = userProfileRepository;
        this.userAccountRepository = userAccountRepository;
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

    // RequestBody: {"username":"a","email":"a@a.com","password":"a","firstName":"fn1","lastName":"ln1","dateOfBirth":"2023-05-03","address":"address1","title":"customer"}
    @PostMapping("/create")
    public String Create(@RequestBody String json)
    {
        System.out.println("Method create() called.");
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String username = jsonNode.get("username").asText();
            String email = jsonNode.get("email").asText();
            String password = jsonNode.get("password").asText();
            String firstName = jsonNode.get("firstName").asText();
            String lastName = jsonNode.get("lastName").asText();
            String address = jsonNode.get("address").asText();
            LocalDate dateOfBirth = LocalDate.parse(jsonNode.get("dateOfBirth").asText());
            String title = jsonNode.get("title").asText();
            userAccountImpl.createUserAccount(username, email, password, firstName, lastName, address, dateOfBirth, title);
            System.out.println("User account " + username + " created successfully");
            return "User account " + username + " created successfully";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/create-customer")
    public String CreateCustomer(@RequestBody String json)
    {
        System.out.println("Method CreateCustomer() called.");
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String username = jsonNode.get("username").asText();
            String email = jsonNode.get("email").asText();
            String password = jsonNode.get("password").asText();
            String firstName = jsonNode.get("firstName").asText();
            String lastName = jsonNode.get("lastName").asText();
            String address = jsonNode.get("address").asText();
            LocalDate dateOfBirth = LocalDate.parse(jsonNode.get("dateOfBirth").asText());
            userAccountImpl.createUserAccount(username, email, password, firstName, lastName, address, dateOfBirth, "customer");
            System.out.println("Registration for " + username + " is successful!");
            return "Registration for " + username + " is successful!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    /// TODO: update these methods below -----------------------------------------------------------------------------

    // http://localhost:8080/api/user-account/deleteUserAccount
    // Example: http://localhost:8080/api/user-account/deleteUserAccount/marcus
    // PHP CURLOPT_POSTFIELDS: username=marcus
    @DeleteMapping("/deleteUserAccount")
    public String deleteUserAccount(@RequestParam("username") String username)
    {
        System.out.println("Method called." + LocalDateTime.now()
                                                           .format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        System.out.println();

        System.out.println("Username: " + username);
        System.out.println();

        try {
            int size = userAccountRepository.findAll().size();
            UserAccount userAccount = userAccountRepository.findUserAccountByUsername(username);
            if (userAccount == null) {
                System.out.println("User account " + username + " not found");
                return "User account " + username + " not found";
            }
            userAccountRepository.delete(userAccount);
            if (userAccountRepository.findAll().size() == size - 1) {
                System.out.println("User account " + username + " deleted successfully");
                return "User account " + username + " deleted successfully";
            } else {
                System.out.println("User account " + username + " failed to delete");
                return "User account " + username + " failed to delete";
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // curl -X GET http://localhost:8080/api/user-profile/readAllUserAccounts
    @GetMapping("/readAllUserAccounts")
    public String readAllUserAccounts()
    {
        return userAccountImpl.getManagerUserAccounts().toString();
    }
}
