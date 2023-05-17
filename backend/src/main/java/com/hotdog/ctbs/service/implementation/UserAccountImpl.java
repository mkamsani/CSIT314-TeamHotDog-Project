package com.hotdog.ctbs.service.implementation;

import com.hotdog.ctbs.entity.UserAccount;
import com.hotdog.ctbs.entity.UserProfile;
import com.hotdog.ctbs.repository.UserAccountRepository;
import com.hotdog.ctbs.repository.UserProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAccountImpl {

    final UserAccountRepository userAccountRepo;
    final UserProfileRepository userProfileRepo;

    public UserAccountImpl(UserAccountRepository userAccountRepo,
                           UserProfileRepository userProfileRepo)
    {
        this.userAccountRepo = userAccountRepo;
        this.userProfileRepo = userProfileRepo;
    }

    //////////////////////////////// Accessors ////////////////////////////////

    @Transactional
    public List<UserAccount> getUserAccountsByPrivilege(String privilege)
    {
        if (!privilege.equals("admin") && !privilege.equals("customer") &&
            !privilege.equals("owner") && !privilege.equals("manager"))
            throw new IllegalArgumentException("Invalid privilege.");
        List<UserProfile> userProfiles = userProfileRepo.findUserProfilesByPrivilege(privilege);
        if (userProfiles == null)
            throw new IllegalArgumentException("No user accounts exist with given privilege.");

        return userProfiles.stream().flatMap(userProfile -> userProfile.getUserAccounts().stream()).toList();
    }
}
