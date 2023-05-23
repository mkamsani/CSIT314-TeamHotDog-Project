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

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/customer/user-account")
public class CustomerAccountUpdateController {

    private final UserAccountRepository userAccountRepo;
    private final UserProfileRepository userProfileRepo;
    private final ObjectMapper objectMapper;

    public CustomerAccountUpdateController(UserAccountRepository userAccountRepo,
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
                                          "customer"
            );
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
