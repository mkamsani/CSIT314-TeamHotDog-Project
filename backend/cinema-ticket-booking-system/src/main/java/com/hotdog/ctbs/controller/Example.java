package com.hotdog.ctbs.controller;

import com.hotdog.ctbs.entity.UserAccount;
import com.hotdog.ctbs.repository.UserAccountRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tests")
public class Example {

    @Autowired
    private UserAccountRepository userAccountRepository;

    Faker faker = new Faker();

    @GetMapping("/greeting")
    public String getHello() {
        return "Hello, time is " + OffsetDateTime.now();
    }

    @GetMapping("/CRUD/Create/faker")
    public String createUserAccountFake() {
        int currentSize = userAccountRepository.findAll().size();
        UserAccount userAccount = UserAccount.fake(currentSize + 1);
        userAccountRepository.save(userAccount);
        return String.valueOf(userAccountRepository.findAll().size());
    }

    @GetMapping("/CRUD/Create/{username}/{passwordHash}/{firstName}/{lastName}/{email}/{address}")
    public void createUserAccount(@PathVariable String username,
                                  @PathVariable String passwordHash,
                                  @PathVariable String firstName,
                                  @PathVariable String lastName,
                                  @PathVariable String email,
                                  @PathVariable String address)
    {
        int currentSize = userAccountRepository.findAll().size();
        UserAccount userAccount = new UserAccount(currentSize + 1,
                username,
                "customer",
                // convert password to bcrypt hash
                passwordHash,
                firstName,
                lastName,
                email,
                address,
                OffsetDateTime.now(),
                OffsetDateTime.now());
        userAccountRepository.save(userAccount);
    }

    @GetMapping("/faker/UserAccounts")
    public String getSelectStatement() {

        // Generate dummy data.
        List<UserAccount> userAccounts = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            Integer userId = i;
            String username = faker.name().firstName().toLowerCase() + faker.number().digits(2);
            String accountType = "customer";
            String passwordHash = faker.number().digits(10);
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = faker.internet().emailAddress();
            String address = faker.address().streetAddress();

            userAccounts.add(new UserAccount(userId,
                    username,
                    accountType,
                    passwordHash,
                    firstName,
                    lastName,
                    email,
                    address,
                    OffsetDateTime.now(),
                    OffsetDateTime.now()));
        }
        userAccountRepository.saveAll(userAccounts);
        return userAccounts.toString();
    }
}