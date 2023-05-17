package com.hotdog.ctbs.service.implementation;

import com.hotdog.ctbs.entity.UserProfile;
import com.hotdog.ctbs.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * CRUD for user profiles.
 *
 * @see com.hotdog.ctbs.entity.UserProfile
 */
@Service
public class UserProfileImpl {

    private final UserProfileRepository userProfileRepo;

    public UserProfileImpl(UserProfileRepository userProfileRepo)
    {
        this.userProfileRepo = userProfileRepo;
    }

    public void create(String privilege, String title)
    {
        for (String s : getAllTitles())
            if (s.equalsIgnoreCase(title))
                throw new IllegalArgumentException("Title already exists.");
        for (String s : new String[]{ "admin", "owner", "manager", "customer",
                                      "titles", "privileges", "active", "all"
        }) if (s.equalsIgnoreCase(title))
            throw new IllegalArgumentException("Reserved title.");
        if (privilege.equals("customer"))
            throw new IllegalArgumentException("Reserved privilege.");
        if (!getValidPrivileges().contains(privilege))
            throw new IllegalArgumentException("Invalid privilege.");
        if (!title.matches("^[a-zA-Z ]+$"))
            throw new IllegalArgumentException("Title must contain only letters and spaces.");

        title = title.strip().replaceAll("\\s+", " ");

        userProfileRepo.save(
                UserProfile.builder()
                           .id(UUID.randomUUID())
                           .privilege(privilege)
                           .title(title)
                           .isActive(true)
                           .build()
        );
    }

    public List<UserProfile> getActiveUserProfiles()
    {
        return userProfileRepo.findAll().stream()
                              .filter(UserProfile::getIsActive)
                              .toList();
    }

    public List<UserProfile> getAllUserProfiles()
    {
        return userProfileRepo.findAll();
    }

    /** @return [ "junior manager", ... , "chief information officer" ] */
    public List<String> getAllTitles()
    {
        return userProfileRepo.findAll().stream().map(UserProfile::getTitle).toList();
    }

    /** @return [ "admin", "owner", "manager", "customer" ] */
    public List<String> getAllPrivileges()
    {
        return userProfileRepo.findAll().stream()
                              .map(UserProfile::getPrivilege)
                              .distinct()
                              .toList();
    }

    /** @return [ "admin", "owner", "manager" ] */
    public List<String> getValidPrivileges()
    {
        return userProfileRepo.findAll().stream()
                              .map(UserProfile::getPrivilege)
                              .distinct()
                              .filter(privilege -> !privilege.equals("customer"))
                              .toList();
    }

    public List<UserProfile> getUserProfilesByPrivilege(final String privilege)
    {
        return userProfileRepo.findAll().stream()
                              .filter(userProfile -> userProfile.getPrivilege().equals(privilege))
                              .toList();
    }

    public UserProfile getUserProfileByTitle(final String title)
    {
        return userProfileRepo.findUserProfileByTitle(title).orElseThrow(
                () -> new IllegalArgumentException("User profile not found.")
        );
    }
}
