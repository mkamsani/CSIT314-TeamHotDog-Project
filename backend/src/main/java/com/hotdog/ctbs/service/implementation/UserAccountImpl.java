package com.hotdog.ctbs.service.implementation;

import com.hotdog.ctbs.entity.UserAccount;
import com.hotdog.ctbs.entity.UserProfile;
import com.hotdog.ctbs.repository.UserAccountRepository;
import com.hotdog.ctbs.repository.UserProfileRepository;
import com.hotdog.ctbs.service.UserAccountService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

/**
 *
 */
@Service
public class UserAccountImpl implements UserAccountService {

    final UserAccountRepository userAccountRepo;
    final UserProfileRepository userProfileRepo;

    public UserAccountImpl(UserAccountRepository userAccountRepo,
                           UserProfileRepository userProfileRepo)
    {
        this.userAccountRepo = userAccountRepo;
        this.userProfileRepo = userProfileRepo;
    }

    @Transactional
    public UserAccount findUserAccountByUsername(final String username)
    {
        return userAccountRepo.findUserAccountByUsername(username);
    }

    @Transactional
    public String login(final String username, final String password)
    {
        UserAccount userAccount = userAccountRepo.findUserAccountByUsername(username);
        if (userAccount == null || !userAccountRepo.existsUserAccountByUsernameAndPassword(username, password))
            throw new IllegalArgumentException("Invalid username or password.");

        String privilege = userAccount.getUserProfile().getPrivilege();
        if (privilege == null)
            throw new IllegalArgumentException("Invalid privilege.");
        else if (!privilege.equals("admin") &&
                 !privilege.equals("customer") &&
                 !privilege.equals("owner") &&
                 !privilege.equals("manager"))
            throw new IllegalArgumentException("Invalid privilege.");

        // Update last login time.
        userAccount.setTimeLastLogin(OffsetDateTime.now());
        userAccountRepo.save(userAccount);

        // Return URL to user's profile page.
        return privilege;

        // Return values are:
        // "Invalid username or password."
        // "Invalid privilege."
        // "admin" || "customer" || "owner" || "manager"
    }

    public void createUserAccount(final String username, final String password, final String email,
                                    final String firstName, final String lastName, final String address,
                                    final LocalDate dateOfBirth,
                                    final String title)
    {
        // The following validations are required when an admin/customer creates a new user account.
        if (userAccountRepo.findUserAccountByUsername(username) != null)
            throw new IllegalArgumentException("Username " + username + " already exists.");
        if (userAccountRepo.findUserAccountByEmail(email) != null)
            throw new IllegalArgumentException("Email " + email + " already exists.");
        if (!username.matches("[a-zA-Z0-9]+"))
            throw new IllegalArgumentException("Username " + username + " must only contain alphanumeric characters.");

        // This validation is only required when an admin creates a new user account.
        UserProfile userProfile = userProfileRepo.findUserProfileByTitle(title);
        if (userProfile == null)
            throw new IllegalArgumentException("Invalid title.");

        try {
            UserAccount userAccount = UserAccount.builder()
                                                 // Default values.
                                                 .isActive(true)
                                                 .timeCreated(OffsetDateTime.now())
                                                 .timeLastLogin(OffsetDateTime.now())
                                                 .userProfile(userProfile)
                                                 // User input values.
                                                 .username(username.toLowerCase())
                                                 .passwordHash(password) // Hashed in Postgres.
                                                 .email(email.toLowerCase())
                                                 .firstName(firstName)
                                                 .lastName(lastName)
                                                 .address(address)
                                                 .dateOfBirth(dateOfBirth)
                                                 .build();
            userAccountRepo.save(userAccount);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date of birth format: " + dateOfBirth);
        }
    }

    public List<UserAccount> getUserAccountsByTitle(String title)
    {
        UserProfile userProfile = userProfileRepo.findUserProfileByTitle(title);
        if (userProfile == null)
            return null;

        return userProfile.getUserAccounts().stream().toList();
    }

    @Transactional
    public List<UserAccount> getUserAccountsByPrivilege(String privilege)
    {
        List<UserProfile> userProfiles = userProfileRepo.findUserProfilesByPrivilege(privilege);
        if (userProfiles == null)
            return null;

        return userProfiles.stream().flatMap(userProfile -> userProfile.getUserAccounts().stream()).toList();
    }

    @Transactional
    public List<UserAccount> getAdminUserAccounts()
    {
        return getUserAccountsByPrivilege("admin");
    }

    @Transactional
    public List<UserAccount> getOwnerUserAccounts()
    {
        return getUserAccountsByPrivilege("owner");
    }

    @Transactional
    public List<UserAccount> getManagerUserAccounts()
    {
        return getUserAccountsByPrivilege("manager");
    }

    @Transactional
    public List<UserAccount> getCustomerUserAccounts()
    {
        return getUserAccountsByPrivilege("customer");
    }
}
