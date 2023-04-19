package com.hotdog.ctbs.controller;

import com.hotdog.ctbs.entity.UserAccount;
import com.hotdog.ctbs.repository.UserAccountRepository;
import com.hotdog.ctbs.repository.UserProfileRepository;
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

    @Autowired
    private UserProfileRepository userProfileRepository;

    Faker faker = new Faker();

    @GetMapping("/greeting")
    public String getHello() {
        return "Hello, time is " + OffsetDateTime.now();
    }

    @GetMapping("/CRUD/Read")
    public List<UserAccount> getUserAccounts() {
        return userAccountRepository.findAll();
    }

    /*
    @GetMapping("/CRUD/Create/faker")
    public String createUserAccountFake() {
        int currentSize = userAccountRepository.findAll().size();
        UserAccount userAccount = UserAccount.fake(currentSize + 1);
        userAccountRepository.save(userAccount);
        return String.valueOf(userAccountRepository.findAll().size());
    }
    */

    /*
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
    */
}