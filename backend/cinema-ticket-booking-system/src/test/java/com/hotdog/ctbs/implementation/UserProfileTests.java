package com.hotdog.ctbs.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.hotdog.ctbs.entity.*;
import com.hotdog.ctbs.service.implementation.*;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

/**
 * Tests for {@link UserProfileImpl}.
 */
@SpringBootTest
public class UserProfileTests {

    @Autowired
    private UserProfileImpl userProfileImpl;

    static long testStarter()
    {
        // Print "TEST START" in yellow.
        if (System.getProperty("os.name").toLowerCase().contains("windows"))
            System.out.printf("%n\033[33mTEST START\033[0m%n%n");
        else
            System.out.printf("%n\u001B[33mTEST START\u001B[0m%n%n");
        return System.currentTimeMillis();
    }

    static void testEnder(long time)
    {
        if (System.getProperty("os.name").toLowerCase().contains("windows"))
            System.out.printf("\033[33mTime elapsed: %d ms\033[0m%n%n", System.currentTimeMillis() - time);
        else
            System.out.printf("\u001B[33mTime elapsed: %d ms\u001B[0m%n%n", System.currentTimeMillis() - time);
    }

    /** The "U" in "CRUD". */
    @Test
    void getters()
    {
        long time = testStarter();

        System.out.println("userProfileImpl.getAllTitles()");
        System.out.println(userProfileImpl.getAllTitles());
        System.out.println();

        System.out.println("userProfileImpl.getAllPrivileges()");
        System.out.println(userProfileImpl.getAllPrivileges());
        System.out.println();

        System.out.println("userProfileImpl.getValidPrivileges()");
        System.out.println(userProfileImpl.getValidPrivileges());
        System.out.println();

        System.out.println("userProfileImpl.getUserProfilesByPrivilege(\"admin\")");
        System.out.println(userProfileImpl.getUserProfilesByPrivilege("admin"));
        System.out.println();

        System.out.println("userProfileImpl.getUserProfileByTitle(\"Customer\")");
        UserProfile getUserProfileByTitle = userProfileImpl.getUserProfileByTitle("Customer");
        System.out.println(getUserProfileByTitle);
        System.out.println();

        System.out.println("userProfileImpl.getUserProfileById(id)");
        UUID getUserProfileById = getUserProfileByTitle.getId();
        System.out.println(userProfileImpl.getUserProfileById(getUserProfileById));
        System.out.println();

        System.out.println("userProfileImpl.getIdByTitle(\"Customer\")");
        UUID getIdByTitle = userProfileImpl.getIdByTitle("Customer");
        System.out.println(getIdByTitle);
        System.out.println();

        // Perform equality check for the previous two methods.
        System.out.println("getUserProfileById.equals(getIdByTitle)");
        System.out.println(getUserProfileById.equals(getIdByTitle));
        System.out.println();

        // Asserts.
        Assert.isTrue(
                getUserProfileByTitle.getTitle().equals("Customer"),
                "getUserProfileByTitle.getTitle() is not \"Customer\""
        );
        Assert.isTrue(
                getUserProfileByTitle.getPrivilege().equals("customer"),
                "getUserProfileByTitle.getPrivilege() is not \"Customer\""
        );
        Assert.isTrue(
                getUserProfileById.equals(getIdByTitle),
                "getUserProfileById is not getIdByTitle"
        );

        testEnder(time);
    }

    /** The "CUD" in "CRUD". */
    @Test
    void others()
    {
        long time = testStarter();

        Runnable getAllTitles = () -> {
            System.out.println("userProfileImpl.getAllTitles()");
            System.out.println(userProfileImpl.getAllTitles());
            System.out.println();
        };

        System.out.println("userProfileImpl.createByTitle(\"Test Manager\", \"manager\")");
        userProfileImpl.createUserProfile("manager", "Test Manager");
        System.out.println();
        getAllTitles.run();

        System.out.println("userProfileImpl.updateOneTitle(\"Test Manager\", \"Test Manager Updated\")");
        userProfileImpl.updateOneByTitle("Test Manager", "Test Manager Updated");
        System.out.println();
        getAllTitles.run();

        System.out.println("userProfileImpl.updateOnePrivilege(\"Test Manager Updated\", \"admin\")");
        userProfileImpl.updateOneByPrivilege("Test Manager Updated", "admin");
        System.out.println();
        getAllTitles.run();

        System.out.println("userProfileImpl.deleteByTitle(\"Test Manager Updated\")");
        userProfileImpl.deleteByTitle("Test Manager Updated");
        System.out.println();
        getAllTitles.run();

        testEnder(time);
    }

    @Test
    void json() throws JsonProcessingException
    {
        long time = testStarter();

        System.out.println("userProfileFromJSON()");
        UserProfile userProfileFromJSON = userProfileImpl.userProfileFromJSON(
                """
                        {
                            "id": "00000000-0000-0000-0000-000000000000",
                            "title": "Customer",
                            "privilege": "customer"
                        }
                        """
        );
        System.out.println(userProfileFromJSON);
        System.out.println();

        System.out.println("userProfileToJSON()");
        String userProfileToJSON = userProfileImpl.userProfileToJSON(userProfileFromJSON);
        System.out.println(userProfileToJSON);
        System.out.println();

        System.out.println("userProfilesFromJSON()");
        List<UserProfile> userProfilesFromJSON = userProfileImpl.userProfilesFromJSON(
                """
                        [
                             {
                                   "id": "00000000-0000-0000-0000-000000000000",
                                   "title": "Customer",
                                   "privilege": "customer"
                             },
                             {
                                   "id": "00000000-0000-0000-0000-000000000001",
                                   "title": "Manager",
                                   "privilege": "manager"
                             },
                             {
                                   "id": "00000000-0000-0000-0000-000000000002",
                                   "title": "Owner",
                                   "privilege": "owner"
                             },
                             {
                                   "id": "00000000-0000-0000-0000-000000000003",
                                   "title": "Admin",
                                   "privilege": "admin"
                             },
                             {
                                   "id": "00000000-0000-0000-0000-000000000004",
                                   "title": "Professor of the Dark Arts",
                                   "privilege": "Lord Voldemort"
                             }
                        ]
                        """
        );
        System.out.println(userProfilesFromJSON);
        System.out.println();

        System.out.println("userProfilesToJSON()");
        String userProfilesToJSON = userProfileImpl.userProfilesToJSON(userProfilesFromJSON);
        System.out.println(userProfilesToJSON);
        System.out.println();

        System.out.println("userProfileOmitIdToJSON()");
        String userProfileOmitIdToJSON = userProfileImpl.UserProfileResponse(userProfileFromJSON);
        System.out.println(userProfileOmitIdToJSON);
        System.out.println();

        System.out.println("userProfilesOmitIdToJSON()");
        String userProfilesOmitIdToJSON = userProfileImpl.UserProfilesResponse(userProfilesFromJSON);
        System.out.println(userProfilesOmitIdToJSON);
        System.out.println();

        // For this to work as expected, the default entries need to be loaded from schema.sql.
        System.out.println("userProfileOmitIdFromJSON()");
        UserProfile userProfileOmitIdFromJSON = userProfileImpl.UserProfileRequest(
                "{\"title\":\"Customer\",\"privilege\":\"customer\"}"
        );
        System.out.println(userProfileOmitIdFromJSON);
        System.out.println();

        // For this to work as expected, the default entries need to be loaded from schema.sql.
        System.out.println("userProfilesOmitIdFromJSON()");
        List<UserProfile> userProfilesOmitIdFromJSON = userProfileImpl.UserProfilesRequest(
                """
                        [
                             {"title":"Customer","privilege":"customer"},
                             {"title":"JuniorManager","privilege":"manager"},
                             {"title":"SeniorManager","privilege":"owner"},
                             {"title":"ChiefFinancialOfficer","privilege":"owner"},
                             {
                                   "title": "Chief Executive Officer",
                                   
                                   "privilege": "the number of whitespace or newlines doesn't matter."
                             },
                             {
                                   "privilege": "the order of keys doesn't matter,
                                                 but the comma after the closing double quote is required.",
                                   "title"    : "Junior Admin"
                             },
                             {
                                   "id": "00000000-0000-0000-0000-000000000000",
                                   "title": "Senior Admin",
                                   "privilege":
                                   "the id doesn't match any entry in Postgres,
                                   but this works as each title is unique."
                             },
                             {
                                   "id": null,
                                   "title": "Chief Information Officer",
                                   "privilege":
                                   "the id is null,
                                   but this works as each title is unique."
                             }
                        ]
                        """
        );
        // If title is a super-key, why do we use UUID as the primary key?
        // Why not just use title?
        // Because the following can happen:
        // UPDATE user_profile SET title = 'Senior Admin' WHERE title = 'Junior Admin';
        // UPDATE user_profile SET title = 'Chief Executive Officer' WHERE title = 'Senior Admin';
        // UPDATE user_profile SET title = 'Junior Admin' WHERE title = 'Chief Executive Officer';
        System.out.println(userProfilesOmitIdFromJSON);
        System.out.println();

        // Asserts.
        Assert.isTrue(
                userProfileFromJSON.getTitle().equals("Customer"),
                "userProfileFromJSON.getTitle() is not \"Customer\""
        );
        Assert.isTrue(
                userProfileFromJSON.getPrivilege().equals("customer"),
                "userProfileFromJSON.getPrivilege() is not \"customer\""
        );
        Assert.isTrue(
                userProfileFromJSON.getId().equals(UUID.fromString("00000000-0000-0000-0000-000000000000")),
                "userProfileFromJSON.getId() is not \"00000000-0000-0000-0000-000000000000\""
        );
        Assert.isTrue(
                userProfilesFromJSON.size() == 5,
                "userProfilesFromJSON.size() is not 5"
        );

        testEnder(time);
    }
}
