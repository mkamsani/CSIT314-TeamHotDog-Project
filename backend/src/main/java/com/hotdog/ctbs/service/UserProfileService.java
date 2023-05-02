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

    /** SELECT title FROM user_profile; */
    List<String> getAllTitles();

    /** SELECT DISTINCT privilege FROM user_profile; */
    List<String> getAllPrivileges();

    /** SELECT DISTINCT privilege FROM user_profile WHERE privilege != 'customer'; */
    List<String> getValidPrivileges();

    /** SELECT * FROM user_profile WHERE privilege = arg; */
    List<UserProfile> getUserProfilesByPrivilege(String privilege);

    /** SELECT * FROM user_profile WHERE id = arg; */
    UserProfile getUserProfileById(UUID id);

    /** SELECT * FROM user_profile WHERE title = arg; */
    UserProfile getUserProfileByTitle(String title);

    /** SELECT id FROM user_profile WHERE title = arg; */
    UUID getIdByTitle(String title);

    /** INSERT INTO user_profile (privilege, title) VALUES (arg, arg); */
    void createUserProfile(String title, String privilege);

    /** UPDATE user_profile SET title = newTitle WHERE title = targetTitle; */
    void updateOneByTitle(String oldTitle, String newTitle);

    /**
     * UPDATE user_profile SET privilege = newPrivilege WHERE title = targetTitle<br />
     * AND (newPrivilege = 'admin' OR newPrivilege = 'owner' OR newPrivilege = 'manager')
     */
    void updateOneByPrivilege(String targetTitle, String privilege);

    /** DELETE FROM user_profile WHERE title = arg */
    String suspendOneByTitle(String title);

    /** Send JSON to frontend, without UUID. */
    String UserProfileResponse(UserProfile userProfile) throws JsonProcessingException;

    /** Send JSON to frontend, without UUID. */
    String UserProfilesResponse(List<UserProfile> userProfiles) throws JsonProcessingException;

    /** Receive JSON from frontend, regardless of UUID (or lack thereof). */
    UserProfile UserProfileRequest(String json) throws JsonProcessingException;

    /** Receive JSON from frontend, regardless of UUID (or lack thereof). */
    List<UserProfile> UserProfilesRequest(String json) throws JsonProcessingException;
}
