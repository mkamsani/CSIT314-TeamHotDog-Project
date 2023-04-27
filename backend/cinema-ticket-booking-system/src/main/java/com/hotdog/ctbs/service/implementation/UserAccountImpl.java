package com.hotdog.ctbs.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotdog.ctbs.entity.UserAccount;
import com.hotdog.ctbs.entity.UserProfile;
import com.hotdog.ctbs.repository.UserAccountRepository;
import com.hotdog.ctbs.repository.UserProfileRepository;
import com.hotdog.ctbs.service.UserAccountSvc;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hotdog.ctbs.service.UserProfileSvc;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 *
 */
@Service
public class UserAccountImpl implements UserAccountSvc {

    final UserAccountRepository userAccountRepository;
    final UserProfileRepository userProfileRepository;

    public UserAccountImpl(UserAccountRepository userAccountRepository,
                           UserProfileRepository userProfileRepository)
    {
        this.userAccountRepository = userAccountRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @Transactional
    public UserAccount findUserAccountByUsername(final String username)
    {
        return userAccountRepository.findUserAccountByUsername(username);
    }

    /**
     * An invalid login will always return "Invalid username or password."
     * <br />
     * Reason: System should not expose if a username exists or not.
     * @param username username
     * @param password plaintext password
     * @return "Invalid username or password." or a URL to the user's profile page
     */
    @Transactional
    public String login(final String username, final String password)
    {
        UserAccount userAccount = userAccountRepository.findUserAccountByUsername(username);
        if (userAccount != null && userAccountRepository.existsUserAccountByUsernameAndPassword(username, password)) {
            userAccount.setTimeLastLogin(OffsetDateTime.now());
            userAccountRepository.save(userAccount);
            return switch (userAccount.getUserProfile().getPrivilege()) {
                case "manager" -> "https://raw.github.com/manager.php";
                case "owner" -> "https://raw.github.com/owner.php";
                case "admin" -> "https://raw.github.com/admin.php";
                default -> "https://raw.github.com/user.php";
            };
        }
        return "Invalid username or password.";
    }
}
