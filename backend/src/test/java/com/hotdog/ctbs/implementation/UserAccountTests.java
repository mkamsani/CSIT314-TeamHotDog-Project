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
import java.util.Objects;
import java.util.UUID;

@SpringBootTest
public class UserAccountTests {

    @Autowired
    UserAccountImpl userAccountImpl;

    @Test
    void getters()
    {
        System.out.println("userAccountImpl.findUserAccountByUsername(\"stonebraker\")");
        System.out.println(userAccountImpl.findUserAccountByUsername("stonebraker"));
        System.out.println();

        // Get all admins.
        System.out.println("userAccountImpl.getUserAccountsByPrivilege(\"admin\")");
        List<UserAccount> getUserAccsByPrivilegeAdmin = userAccountImpl.getUserAccountsByPrivilege("admin");
        System.out.println(getUserAccsByPrivilegeAdmin);
        System.out.println("Count: " + getUserAccsByPrivilegeAdmin.size());
        System.out.println();

        // Get all managers.
        System.out.println("userAccountImpl.getUserAccountsByPrivilege(\"manager\")");
        List<UserAccount> getUserAccsByPrivilegeManager = userAccountImpl.getUserAccountsByPrivilege("manager");
        System.out.println(getUserAccsByPrivilegeManager);
        System.out.println("Count: " + getUserAccsByPrivilegeManager.size());
        System.out.println();

        // Get all owners.
        System.out.println("userAccountImpl.getUserAccountsByPrivilege(\"owner\")");
        List<UserAccount> getUserAccsByPrivilegeOwner = userAccountImpl.getUserAccountsByPrivilege("owner");
        System.out.println(getUserAccsByPrivilegeOwner);
        System.out.println("Count: " + getUserAccsByPrivilegeOwner.size());
        System.out.println();

        // Get all customers.
        System.out.println("userAccountImpl.getUserAccountsByPrivilege(\"customer\")");
        List<UserAccount> getUserAccsByPrivilegeCustomer = userAccountImpl.getUserAccountsByPrivilege("customer");
        System.out.println(getUserAccsByPrivilegeCustomer);
        System.out.println("Count: " + getUserAccsByPrivilegeCustomer.size());
        System.out.println();

        String[] assertMsg = {
                "!getUserAccsByPrivilegeAdmin.equals(userAccountImpl.getAdminUserAccounts())",
                "!getUserAccsByPrivilegeManager.equals(userAccountImpl.getManagerUserAccounts())",
                "!getUserAccsByPrivilegeOwner.equals(userAccountImpl.getOwnerUserAccounts())",
                "!getUserAccsByPrivilegeCustomer.equals(userAccountImpl.getCustomerUserAccounts())"
        };
        Assert.isTrue(getUserAccsByPrivilegeAdmin.equals(userAccountImpl.getAdminUserAccounts()), assertMsg[0]);
        Assert.isTrue(getUserAccsByPrivilegeManager.equals(userAccountImpl.getManagerUserAccounts()), assertMsg[1]);
        Assert.isTrue(getUserAccsByPrivilegeOwner.equals(userAccountImpl.getOwnerUserAccounts()), assertMsg[2]);
        Assert.isTrue(getUserAccsByPrivilegeCustomer.equals(userAccountImpl.getCustomerUserAccounts()), assertMsg[3]);
        System.out.println();

        // Give an invalid privilege.
        System.out.println("userAccountImpl.getUserAccountsByPrivilege(\"invalid\")");
        List<UserAccount> getUserAccsByPrivilegeInvalid = userAccountImpl.getUserAccountsByPrivilege("invalid");
        System.out.println(getUserAccsByPrivilegeInvalid);
        System.out.println("Count: " + getUserAccsByPrivilegeInvalid.size());
        System.out.println();
    }

    @Test
    void login()
    {
        System.out.println();
    }
}
