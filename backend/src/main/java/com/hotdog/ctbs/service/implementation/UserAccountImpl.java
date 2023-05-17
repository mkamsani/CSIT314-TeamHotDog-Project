package com.hotdog.ctbs.service.implementation;

import com.hotdog.ctbs.entity.UserAccount;
import com.hotdog.ctbs.entity.UserProfile;
import com.hotdog.ctbs.repository.UserAccountRepository;
import com.hotdog.ctbs.repository.UserProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

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

    @Transactional
    public void update(final String target,
                       final String username,
                       final String firstName,
                       final String lastName,
                       final String email,
                       final String address,
                       final LocalDate dateOfBirth,
                       final String title)
    {
        UserAccount userAccount = userAccountRepo.findUserAccountByUsername(target).orElseThrow(
                () -> new IllegalArgumentException("User account " + target + " does not exist.")
        );
        UserProfile userProfile = userProfileRepo.findUserProfileByTitle(title).orElseThrow(
                () -> new IllegalArgumentException("Title " + title + " does not exist.")
        );

        if (userAccountRepo.findUserAccountByUsername(username).isPresent() &&
            !userAccount.getUsername().equals(username))
            throw new IllegalArgumentException("Username " + username + " already exists.");
        if (userAccountRepo.findUserAccountByEmail(email).isPresent() &&
            !userAccount.getEmail().equals(email))
            throw new IllegalArgumentException("Email " + email + " already exists.");
        if (!username.matches("[a-zA-Z0-9]+"))
            throw new IllegalArgumentException("Username " + username + " must be alphanumeric.");
        if (username.equals("admin") || username.equals("customer") ||
            username.equals("owner") || username.equals("manager"))
            throw new IllegalArgumentException("Username " + username + " is reserved.");
        if (dateOfBirth.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Invalid date of birth: " + dateOfBirth);

        userAccount.setUsername(username.toLowerCase());
        userAccount.setEmail(email.toLowerCase());
        userAccount.setFirstName(firstName);
        userAccount.setLastName(lastName);
        userAccount.setAddress(address);
        userAccount.setDateOfBirth(dateOfBirth);
        userAccount.setUserProfile(userProfile);
        userAccountRepo.save(userAccount);
    }

    @Transactional
    public void suspend(final String target)
    {
        UserAccount userAccount = userAccountRepo.findUserAccountByUsername(target).orElse(null);
        if (userAccount == null)
            throw new IllegalArgumentException("User account " + target + " does not exist.");
        if (!userAccount.getIsActive())
            throw new IllegalArgumentException("User account " + target + " is already suspended.");

        userAccount.setIsActive(false);
        userAccountRepo.save(userAccount);
    }

    //////////////////////////////// Accessors ////////////////////////////////

    @Transactional
    public UserAccount getUserAccountByUsername(final String username)
    {
        return userAccountRepo.findUserAccountByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("User account does not exist.")
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
            throw new IllegalArgumentException("No user accounts exist with given privilege.");

        return userProfiles.stream().flatMap(userProfile -> userProfile.getUserAccounts().stream()).toList();
    }
}
