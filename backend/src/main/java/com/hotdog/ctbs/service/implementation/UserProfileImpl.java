package com.hotdog.ctbs.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotdog.ctbs.entity.UserProfile;
import com.hotdog.ctbs.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import com.hotdog.ctbs.service.UserProfileService;

import java.util.List;
import java.util.UUID;

/**
 * CRUD for user profiles.
 *
 * @see com.hotdog.ctbs.entity.UserProfile
 */
@Service
public class UserProfileImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public UserProfileImpl(UserProfileRepository userProfileRepository)
    {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public List<String> getAllTitles()
    {
        return userProfileRepository.findAll().stream()
                                    .map(UserProfile::getTitle)
                                    .toList();
    }

    /** @return [ "admin", "owner", "manager", "customer" ] */
    @Override
    public List<String> getAllPrivileges()
    {
        return userProfileRepository.findAll().stream()
                                    .map(UserProfile::getPrivilege)
                                    .distinct()
                                    .toList();
    }

    /** @return [ "admin", "owner", "manager"] */
    @Override
    public List<String> getValidPrivileges()
    {
        return userProfileRepository.findAll().stream()
                                    .map(UserProfile::getPrivilege)
                                    .distinct()
                                    .filter(privilege -> !privilege.equals("customer"))
                                    .toList();
    }

    @Override
    public List<UserProfile> getUserProfilesByPrivilege(final String privilege)
    {
        return userProfileRepository.findAll().stream()
                                    .filter(userProfile -> userProfile.getPrivilege().equals(privilege))
                                    .toList();
    }

    @Override
    public UserProfile getUserProfileById(final UUID id)
    {
        return userProfileRepository.findById(id)
                                    .orElse(null);
    }

    @Override
    public UserProfile getUserProfileByTitle(final String title)
    {
        return userProfileRepository.findUserProfileByTitle(title);
    }

    @Override
    public UUID getIdByTitle(final String title)
    {
        for (UserProfile userProfile : userProfileRepository.findAll())
            if (userProfile.getTitle().equals(title))
                return userProfile.getId();
        return null;
    }

    @Override
    public void createUserProfile(String privilege, String title)
    {
        for (String s : getAllTitles())
            if (s.equalsIgnoreCase(title))
                throw new IllegalArgumentException("Title already exists.");
        if (privilege.equals("customer"))
            throw new IllegalArgumentException("Reserved privilege.");
        if (!getValidPrivileges().contains(privilege))
            throw new IllegalArgumentException("Invalid privilege.");
        if (!title.matches("^[a-zA-Z ]+$"))
            throw new IllegalArgumentException("Title must contain only letters and spaces.");

        title = title.strip().replaceAll("\\s+", " ");

        userProfileRepository.save(
                UserProfile.builder()
                           .id(UUID.randomUUID())
                           .privilege(privilege)
                           .title(title)
                           .build()
        );
    }

    @Override
    public void updateOneByTitle(String targetTitle, String newTitle)
    {
        if (targetTitle.equals("Customer"))
            throw new IllegalArgumentException("Cannot modify Customer.");
        for (UserProfile userProfile : userProfileRepository.findAll()) {
            if (!userProfile.getTitle().equals(targetTitle))
                continue;

            if (!newTitle.matches("^[a-zA-Z ]+$"))
                throw new IllegalArgumentException("Title must contain only letters and spaces.");

            newTitle = newTitle.strip().replaceAll("\\s+", " ");

            userProfile.setTitle(newTitle);
            userProfileRepository.save(userProfile);
            break;
        }
    }

    /**
     * @param targetTitle  the title of the user profile to be modified.
     * @param newPrivilege the new privilege.
     */
    @Override
    public void updateOneByPrivilege(String targetTitle, String newPrivilege)
    {
        if (targetTitle.equals("Customer"))
            throw new IllegalArgumentException("Cannot modify Customer.");
        for (UserProfile userProfile : userProfileRepository.findAll()) {
            if (!userProfile.getTitle().equals(targetTitle))
                continue;

            newPrivilege = newPrivilege.strip().replaceAll("\\s+", " ");

            if (!newPrivilege.matches("^[a-zA-Z ]+$"))
                throw new IllegalArgumentException("Privilege must contain only letters and spaces.");
            userProfile.setPrivilege(newPrivilege);
            userProfileRepository.save(userProfile);
            break;
        }
    }

    @Override
    public String suspendOneByTitle(String title)
    {
        UserProfile userProfile = userProfileRepository.findUserProfileByTitle(title);
        if (userProfile == null)
            return "Not found.";
        if (!userProfile.getIsActive())
            return title + " is already suspended.";

        userProfile.setIsActive(false);
        userProfileRepository.save(userProfile);

        int size = userProfile.getUserAccounts().size();
        if (size == 0)
            return title + " has been suspended.";
        else
            return title + " has been suspended. " + size + " user account(s) have been suspended.";
    }

    @Override
    public String UserProfileResponse(UserProfile userProfile) throws JsonProcessingException
    {
        ObjectMapper om = new ObjectMapper();
        JsonNode jn = om.readTree(om.writeValueAsString(userProfile));
        ((ObjectNode) jn).remove("id");
        return jn.toString();
    }

    @Override
    public String UserProfilesResponse(List<UserProfile> userProfiles) throws JsonProcessingException
    {
        StringBuilder sb = new StringBuilder("[");
        for (UserProfile userProfile : userProfiles)
            sb.append(UserProfileResponse(userProfile))
              .append(",");

        return sb.deleteCharAt(sb.length() - 1)
                 .append("]")
                 .toString();
    }

    @Override
    public UserProfile UserProfileRequest(String json) throws JsonProcessingException
    {
        return userProfileRepository.findUserProfileByTitle(
                new ObjectMapper().readValue(json, UserProfile.class).getTitle()
        );
    }

    @Override
    public List<UserProfile> UserProfilesRequest(String json) throws JsonProcessingException
    {
        List<UserProfile> userProfiles =
                new ObjectMapper().readValue(json, new TypeReference<>() {
        });
        return userProfiles.stream()
                           .map(userProfile -> userProfileRepository.findUserProfileByTitle(userProfile.getTitle()))
                           .toList();
    }

    @Override
    public void updateUserProfile(String uuid, String privilege, String title)
    {
        UserProfile userProfile = userProfileRepository.findById(UUID.fromString(uuid))
                                                       .orElse(null);
        if (userProfile == null)
            throw new IllegalArgumentException("User profile not found.");
        if (title.equals("Customer"))
            throw new IllegalArgumentException("Cannot modify Customer.");
        if (privilege.equals("customer"))
            throw new IllegalArgumentException("Reserved privilege.");
        if (!getValidPrivileges().contains(privilege))
            throw new IllegalArgumentException("Invalid privilege.");
        if (!title.matches("^[a-zA-Z ]+$"))
            throw new IllegalArgumentException("Title must contain only letters and spaces.");

        title = title.strip().replaceAll("\\s+", " ");

        userProfile.setPrivilege(privilege);
        userProfile.setTitle(title);
        userProfileRepository.save(userProfile);
    }
}
