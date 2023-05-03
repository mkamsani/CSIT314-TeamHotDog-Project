package com.hotdog.ctbs.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hotdog.ctbs.entity.UserProfile;

import java.util.List;
import java.util.UUID;

/**
 * Each CRUD method has a corresponding SQL query.
 * <br />
 * "arg" refers to the method parameter.
 */
public interface UserProfileService {

    List<UserProfile> getActiveUserProfiles();

    /** SELECT title FROM user_profile; */
    List<String> getAllTitles();

    /** SELECT DISTINCT privilege FROM user_profile; */
    List<String> getAllPrivileges();

    /** SELECT DISTINCT privilege FROM user_profile WHERE privilege != 'customer'; */
    List<String> getValidPrivileges();

    /** SELECT * FROM user_profile WHERE privilege = arg; */
    List<UserProfile> getUserProfilesByPrivilege(String privilege);

    /** SELECT id FROM user_profile WHERE title = arg; */
    UUID getIdByTitle(String title);

    /** INSERT INTO user_profile (privilege, title) VALUES (arg, arg); */
    void createUserProfile(String title, String privilege);

    /** DELETE FROM user_profile WHERE title = arg */
    String suspendUserProfileByTitle(String title);

    void updateUserProfileByTitle(String uuid, String privilege, String title);
}
