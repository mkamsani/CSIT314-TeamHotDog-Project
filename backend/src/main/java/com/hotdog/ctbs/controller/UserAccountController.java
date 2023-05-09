package com.hotdog.ctbs.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotdog.ctbs.entity.UserAccount;
import com.hotdog.ctbs.repository.UserAccountRepository;
import com.hotdog.ctbs.repository.UserProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hotdog.ctbs.service.implementation.UserAccountImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/user-account")
public class UserAccountController {

    private final UserAccountImpl userAccountImpl;
    private final UserProfileRepository userProfileRepository;
    private final UserAccountRepository userAccountRepository;
    private final Logger logger;

    public UserAccountController(UserAccountImpl userAccountImpl,
                                 UserProfileRepository userProfileRepository,
                                 UserAccountRepository userAccountRepository)
    {
        this.userAccountImpl = userAccountImpl;
        this.userProfileRepository = userProfileRepository;
        this.userAccountRepository = userAccountRepository;
        this.logger = Logger.getLogger(UserAccountController.class.getName());
    }

    public void printMethodStarter(String message)
    {
        logger.info(System.lineSeparator());
        logger.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM d h:mm:ss a")));
        logger.info(message);
        logger.info(System.lineSeparator());
    }

    /**
     * curl -X POST -H "Content-Type: application/json" -d '{"userId":"user_0","password":"password_0"}' http://localhost:8000/api/user-account/login
     */
    @PostMapping("/login")
    public String Login(@RequestBody String json)
    {
        printMethodStarter("Method UserAccountController.Login() called.");
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String username = jsonNode.get("userId").asText();
            System.out.println("username: " + username);
            String password = jsonNode.get("password").asText();
            System.out.println("password: " + password);
            return userAccountImpl.login(username, password);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // RequestBody: {"username":"a","email":"a@a.com","password":"a","firstName":"fn1","lastName":"ln1","dateOfBirth":"2023-05-03","address":"address1","title":"customer"}
    @PostMapping("/create")
    public String Create(@RequestBody String json)
    {
        printMethodStarter("Method UserAccountController.Create() called.");
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
            userAccountImpl.createUserAccount(username, password, email, firstName, lastName, address, dateOfBirth, title);
            System.out.println("User account " + username + " created successfully.");
            return "User account " + username + " created successfully.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/create-customer")
    public String CreateCustomer(@RequestBody String json)
    {
        printMethodStarter("Method UserAccountController.CreateCustomer() called.");
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

    // Array of UserAccount objects,
    // curl -X GET http://localhost:8000/api/user-account/read/all
    // curl -X GET http://localhost:8000/api/user-account/read/active
    // curl -X GET http://localhost:8000/api/user-account/read/admin
    // curl -X GET http://localhost:8000/api/user-account/read/owner
    // curl -X GET http://localhost:8000/api/user-account/read/manager
    // curl -X GET http://localhost:8000/api/user-account/read/customer
    //
    // Singular UserAccount object, inside an array,
    // curl -X GET http://localhost:8000/api/user-account/read/mscott
    // curl -X GET http://localhost:8000/api/user-account/read/stonebraker
    @GetMapping("/read/{param}")
    public String Read(@PathVariable String param)
    {
        printMethodStarter("Method UserAccountController.Read() called.");
        try {
            ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
            List<UserAccount> userAccountList = switch (param) {
                case "admin", "owner", "manager", "customer" -> userAccountImpl.getUserAccountsByPrivilege(param);
                case "active" -> userAccountImpl.getActiveUserAccounts();
                case "all" -> userAccountImpl.getAllUserAccounts();
                // Assume the parameter is a username.
                default -> {
                    List<UserAccount> tmp = new ArrayList<>();
                    // Thrown exception for getUserAccountByUsername() is done in the method itself.
                    tmp.add(userAccountImpl.getUserAccountByUsername(param));
                    yield tmp;
                }
            };
            JsonNode[] jsonNodes = new JsonNode[userAccountList.size()];
            ArrayNode arrayNode = objectMapper.createArrayNode();
            for (int i = userAccountList.size() - 1; i >= 0; i--) {
                jsonNodes[i] = objectMapper.valueToTree(userAccountList.get(i));
                ((ObjectNode) jsonNodes[i]).remove("id");
                ((ObjectNode) jsonNodes[i]).remove("passwordHash");
                ((ObjectNode) jsonNodes[i]).put("title", userAccountList.get(i).getUserProfile().getTitle());
                ((ObjectNode) jsonNodes[i]).put("privilege", userAccountList.get(i).getUserProfile().getPrivilege());
                arrayNode.add(jsonNodes[i]);
            }
            return new ObjectMapper().writeValueAsString(arrayNode);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    /*
curl -X PUT -H "Content-Type: application/json" -d '{"username":"mscott","firstName":"Michael","lastName":"Scott","email":"address":"Scranton, PA","dateOfBirth":"1964-03-15","title":"manager"}' http://localhost:8000/api/user-account/update/mscott
    */
    @PutMapping("/update/{targetUsername}")
    public String Update(@RequestBody String json, @PathVariable String targetUsername)
    {
        printMethodStarter("Method UserAccountController.Update() called.");
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            String username = jsonNode.get("username").asText();
            String firstName = jsonNode.get("firstName").asText();
            String lastName = jsonNode.get("lastName").asText();
            String email = jsonNode.get("email").asText();
            String address = jsonNode.get("address").asText();
            LocalDate dateOfBirth = LocalDate.parse(jsonNode.get("dateOfBirth").asText());
            String title = jsonNode.get("title").asText();
            userAccountImpl.update(targetUsername, username, firstName, lastName, email, address, dateOfBirth, title);
            System.out.println("User account " + username + " updated successfully.");
            return "User account " + username + " updated successfully.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @DeleteMapping("/suspend/{targetUsername}")
    public String Suspend(@PathVariable String targetUsername)
    {
        printMethodStarter("Method UserAccountController.Suspend() called.");
        try {
            userAccountImpl.suspend(targetUsername);
            System.out.println("User account " + targetUsername + " deleted successfully");
            return "User account " + targetUsername + " deleted successfully";
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
        System.out.println("Username: " + username);
        try {
            int size = userAccountRepository.findAll().size();
            UserAccount userAccount = userAccountRepository.findUserAccountByUsername(username).orElse(null);
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
        return null;
    }
}
