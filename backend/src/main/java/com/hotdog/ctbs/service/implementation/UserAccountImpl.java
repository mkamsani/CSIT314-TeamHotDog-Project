package com.hotdog.ctbs.service.implementation;

import com.hotdog.ctbs.entity.UserAccount;
import com.hotdog.ctbs.entity.UserProfile;
import com.hotdog.ctbs.repository.UserAccountRepository;
import com.hotdog.ctbs.repository.UserProfileRepository;
import com.hotdog.ctbs.service.UserAccountService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
            return "{\"url\":\"Invalid username or password.\"}";

        // Update last login time.
        userAccount.setTimeLastLogin(OffsetDateTime.now());
        userAccountRepo.save(userAccount);

        // Return URL to user's profile page.
        return switch (userAccount.getUserProfile().getPrivilege()) {
            case "manager" -> "{\"url\":\"https://raw.github.com/manager.php\"}";
            case "owner" -> "{\"url\":\"https://raw.github.com/owner.php\"}";
            case "admin" -> "{\"url\":\"https://raw.github.com/admin.php\"}";
            default -> "{\"url\":\"https://raw.github.com/user.php\"}";
        };
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
