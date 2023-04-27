package com.hotdog.ctbs.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.hotdog.ctbs.entity.*;
import com.hotdog.ctbs.service.implementation.*;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class UserAccountTests {


    @Test
    /**
     *  Creates dummy user accounts.
     */
    void getters()
    {
        Faker faker = new Faker();
        UserAccount userAccount = new UserAccount();
        userAccount.setPasswordHash(faker.internet().password());
        userAccount.setUsername(faker.name().firstName());
        userAccount.setEmail(faker.internet().emailAddress());
        userAccount.setFirstName(faker.name().firstName());
        userAccount.setLastName(faker.name().lastName());
        userAccount.setAddress(faker.address().fullAddress());
        userAccount.setDateOfBirth(faker.date().birthday().toLocalDateTime().toLocalDate());
        userAccount.setTimeCreated(OffsetDateTime.now());
        userAccount.setTimeLastLogin(OffsetDateTime.now());
        System.out.println(userAccount);
        // {"id":null,"userProfile":null,"isActive":null,"passwordHash":"vc4l4aml","username":"Stan","email":"esteban.mcglynn@gmail.com",
        // "firstName":"Inge","lastName":"Fisher",
        // "address":"Apt. 055 6049 Lashay Vista, Murrayland, NY 94238",
        // "dateOfBirth":[1963,9,9],"timeCreated":1682595428.960024500,"timeLastLogin":1682595428.960024500}
    }
}
