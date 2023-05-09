package com.hotdog.ctbs.implementation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.hotdog.ctbs.entity.*;
import com.hotdog.ctbs.service.implementation.*;

import java.util.List;

@SpringBootTest
public class UserAccountTests {

    @Autowired
    UserAccountImpl userAccountImpl;

    @Test
    void getters()
    {
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
    }

    @Test
    void login()
    {
        System.out.println();
    }
}
