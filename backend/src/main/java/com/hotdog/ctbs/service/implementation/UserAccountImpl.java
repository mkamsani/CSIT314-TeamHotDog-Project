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
import java.util.UUID;

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
    public String login(final String username, final String password)
    {
        UserAccount userAccount = userAccountRepo.findUserAccountByUsername(username).orElse(null);
        if (userAccount == null || !userAccountRepo.existsUserAccountByUsernameAndPassword(username, password))
            throw new IllegalArgumentException("Invalid username or password.");

        String privilege = userAccount.getUserProfile().getPrivilege();
        if (privilege == null)
            throw new IllegalArgumentException("Invalid privilege.");
        if (!privilege.equals("admin") && !privilege.equals("customer") &&
            !privilege.equals("owner") && !privilege.equals("manager"))
            throw new IllegalArgumentException("Invalid privilege.");

        // Update last login time.
        userAccount.setTimeLastLogin(OffsetDateTime.now());
        userAccountRepo.save(userAccount);

        return privilege; // "admin", "customer", "owner", or "manager"
    }

    public void createUserAccount(final String username,
                                  final String password,
                                  final String email,
                                  final String firstName,
                                  final String lastName,
                                  final String address,
                                  final LocalDate dateOfBirth,
                                  final String title)
    {
        System.out.println("Method UserAccountImpl.createUserAccount() called.");

        if (false) { // DEBUG
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
            System.out.println("Email: " + email);
            System.out.println("First name: " + firstName);
            System.out.println("Last name: " + lastName);
            System.out.println("Address: " + address);
            System.out.println("Date of birth: " + dateOfBirth);
            System.out.println("Title: " + title);
        }

        // The following validations are required when an admin/customer creates a new user account.
        if (userAccountRepo.findUserAccountByUsername(username).isPresent())
            throw new IllegalArgumentException("Username " + username + " already exists.");
        if (userAccountRepo.findUserAccountByEmail(email).isPresent())
            throw new IllegalArgumentException("Email " + email + " already exists.");
        if (!username.matches("[a-zA-Z0-9]+"))
            throw new IllegalArgumentException("Username " + username + " must only contain alphanumeric characters.");
        if (username.equals("admin") || username.equals("customer") ||
            username.equals("owner") || username.equals("manager"))
            throw new IllegalArgumentException("Username " + username + " is reserved.");

        // This validation is only required when an admin creates a new user account.
        UserProfile userProfile = userProfileRepo
                .findUserProfileByTitle(title)
                .orElseThrow(() -> new IllegalArgumentException("Invalid title."));

        try {
            userAccountRepo.save(
                    UserAccount.builder()
                               // Default values.
                               .id(UUID.randomUUID())
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
                               .build()
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date of birth format: " + dateOfBirth);
        }
    }

    @Transactional
    public String update(final String target,
                         final String username,
                         final String firstName,
                         final String lastName,
                         final String email,
                         final String address,
                         final LocalDate dateOfBirth,
                         final String title)
    {
        UserAccount userAccount = userAccountRepo.findUserAccountByUsername(target).orElse(null);
        if (userAccount == null)
            throw new IllegalArgumentException("User account " + username + " does not exist.");
        UserProfile userProfile = userProfileRepo.findUserProfileByTitle(title).orElse(null);
        if (userProfile == null)
            throw new IllegalArgumentException("Invalid title.");

        // The following validations are required when an admin/customer updates a user account.
        if (userAccountRepo.findUserAccountByUsername(username).isPresent() &&
            !userAccount.getUsername().equals(username))
            throw new IllegalArgumentException("Username " + username + " already exists.");
        if (userAccountRepo.findUserAccountByEmail(email).isPresent() &&
            !userAccount.getEmail().equals(email))
            throw new IllegalArgumentException("Email " + email + " already exists.");
        if (!username.matches("[a-zA-Z0-9]+"))
            throw new IllegalArgumentException("Username " + username + " must only contain alphanumeric characters.");
        if (username.equals("admin") || username.equals("customer") ||
            username.equals("owner") || username.equals("manager"))
            throw new IllegalArgumentException("Username " + username + " is reserved.");
        userAccount.setUsername(username.toLowerCase());
        userAccount.setEmail(email.toLowerCase());
        userAccount.setFirstName(firstName);
        userAccount.setLastName(lastName);
        userAccount.setAddress(address);
        userAccount.setDateOfBirth(dateOfBirth);
        userAccount.setUserProfile(userProfile);
        userAccountRepo.save(userAccount);
        return "User account " + target + " updated.";
    }

    @Transactional
    public String suspend(final String target)
    {
        UserAccount userAccount = userAccountRepo.findUserAccountByUsername(target).orElse(null);
        if (userAccount == null)
            throw new IllegalArgumentException("User account " + target + " does not exist.");

        userAccount.setIsActive(false);
        userAccountRepo.save(userAccount);

        return "User account " + target + " suspended.";
    }

    @Transactional
    public UserAccount getUserAccountByUsername(final String username)
    {
        return userAccountRepo.findUserAccountByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("User account " + username + " does not exist.")
        );
    }

    public List<UserAccount> getAllUserAccounts()
    {
        return userAccountRepo.findAll();
    }

    public List<UserAccount> getActiveUserAccounts()
    {
        return userAccountRepo.findAll().stream().filter(UserAccount::getIsActive).toList();
    }

    @Transactional
    public List<UserAccount> getUserAccountsByPrivilege(String privilege)
    {
        if (!privilege.equals("admin") && !privilege.equals("customer") &&
            !privilege.equals("owner") && !privilege.equals("manager"))
            throw new IllegalArgumentException("Invalid privilege.");
        List<UserProfile> userProfiles = userProfileRepo.findUserProfilesByPrivilege(privilege).orElse(null);
        if (userProfiles == null)
            return null;

        return userProfiles.stream().flatMap(userProfile -> userProfile.getUserAccounts().stream()).toList();
    }
}
