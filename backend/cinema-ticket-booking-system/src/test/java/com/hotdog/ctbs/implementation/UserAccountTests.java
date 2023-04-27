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

@SpringBootTest
public class UserAccountTests {

    @Autowired
    UserAccountImpl userAccountImpl;

    @Test
    void getters()
    {
        System.out.println("userAccountImpl.findUserAccountByUsername(\"stonebraker\")");
        System.out.println(userAccountImpl.findUserAccountByUsername("stonebraker"));
    }

    @Test
    void login()
    {
        System.out.println();
        String result;

        System.out.println("userAccountImpl.login(\"jim\", \"password_Mgr_is_mgrJ\")");
        result = userAccountImpl.login("jim", "password_Mgr_is_mgrJ");
        System.out.println(result);
        System.out.println();

        System.out.println("userAccountImpl.login(\"mscott\", \"password_Mgr_is_mgrS\")");
        result = userAccountImpl.login("mscott", "password_Mgr_is_mgrS");
        System.out.println(result);
        System.out.println();

        System.out.println("userAccountImpl.login(\"dwallace\", \"password_Owr_is_%CFO\")");
        result = userAccountImpl.login("dwallace", "password_Owr_is_%CFO");
        System.out.println(result);
        System.out.println();

        System.out.println("userAccountImpl.login(\"jbennett\", \"password_Owr_is_%CEO\")");
        result = userAccountImpl.login("jbennett", "password_Owr_is_%CEO");
        System.out.println(result);
        System.out.println();

        System.out.println("userAccountImpl.login(\"marcus\", \"password_Adm_is_admJ\")");
        result = userAccountImpl.login("marcus", "password_Adm_is_admJ");
        System.out.println(result);
        System.out.println();

        System.out.println("userAccountImpl.login(\"samy\", \"password_Adm_is_admS\")");
        result = userAccountImpl.login("samy", "password_Adm_is_admS");
        System.out.println(result);
        System.out.println();

        System.out.println("userAccountImpl.login(\"stonebraker\", \"password_Adm_is_%CIO\")");
        result = userAccountImpl.login("stonebraker", "password_Adm_is_%CIO");
        System.out.println(result);
        System.out.println();
    }
}
