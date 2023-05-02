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
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
            return "{\"response\":\"Invalid username or password.\"}";

        // Update last login time.
        userAccount.setTimeLastLogin(OffsetDateTime.now());
        userAccountRepo.save(userAccount);

        // Return URL to user's profile page.
        return switch (userAccount.getUserProfile().getPrivilege()) {
            case "manager" -> "{\"response\":\"https://raw.github.com/manager.php\"}";
            case "owner" -> "{\"response\":\"https://raw.github.com/owner.php\"}";
            case "admin" -> "{\"response\":\"https://raw.github.com/admin.php\"}";
            default -> "{\"response\":\"https://raw.github.com/user.php\"}";
        };
    }

    public String createCustomerAccount(final String username,
                                        final String password,
                                        final String email,
                                        final String firstName,
                                        final String lastName,
                                        final String address,
                                        final String dateOfBirth)
    {
        return createUserAccount(username, password, email, firstName, lastName, address, dateOfBirth, "customer");
    }

    public String createUserAccount(final String username,
                                    final String password,
                                    final String email,
                                    final String firstName,
                                    final String lastName,
                                    final String address,
                                    final String dateOfBirth,
                                    final String title)
    {
        // The following validations are required when an admin/customer creates a new user account.
        if (userAccountRepo.findUserAccountByUsername(username) != null)
            return "{\"response\":\"Username already exists.\"}";
        if (userAccountRepo.findUserAccountByEmail(email) != null)
            return "{\"response\":\"Email already exists.\"}";
        if (!username.matches("[a-zA-Z0-9]+"))
            return "{\"response\":\"Username must only contain alphanumeric characters.\"}";

        /*
         * The username (AKA "local part"):
         *     Can use these characters: [a-zA-Z0-9-._]
         *     Can't have two dots in a row.
         *     Can't start or end with a dot.
         *
         * The domain:
         *     Can use these characters: [a-zA-Z0-9-.]
         *     Must have one or more dots in it.
         *     Can't have two dots in a row.
         *     Can't start or end with a dot.
         *     Can't start or end with a hyphen.
         *     Must end with a "TLD" that's 2+ letters, only [a-zA-Z] allowed.
         *
         * Reference:
         *     https://colinhacks.com/essays/reasonable-email-regex
         */
        if (!email.matches("^([A-Z0-9_+-]+\\.?)*[A-Z0-9_+-]@([A-Z0-9][A-Z0-9-]*\\.)+[A-Z]{2,}$"))
            return "{\"response\":\"Invalid email format.\"}";

        // This validation is only required when an admin creates a new user account.
        UserProfile userProfile = userProfileRepo.findUserProfileByTitle(title);
        if (userProfile == null)
            return "{\"response\":\"Invalid title.\"}";

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
                                                 .dateOfBirth(LocalDate.parse(dateOfBirth))
                                                 .build();
            userAccountRepo.save(userAccount);
        } catch (Exception e) {
            return "{\"response\":\"Invalid date format.\"}";
        }

        return "{\"response\":\"Account created.\"}";
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
