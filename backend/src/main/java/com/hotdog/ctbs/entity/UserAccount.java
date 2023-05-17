package com.hotdog.ctbs.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hotdog.ctbs.repository.UserAccountRepository;
import com.hotdog.ctbs.repository.UserProfileRepository;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_account")
@JsonIgnoreProperties({"userProfile"})
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", nullable = false)
    private UUID id;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "password_hash", nullable = false, length = 72)
    private String passwordHash;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "time_created", nullable = false)
    private OffsetDateTime timeCreated;

    @Column(name = "time_last_login", nullable = false)
    private OffsetDateTime timeLastLogin;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_profile", nullable = false)
    @Fetch(FetchMode.JOIN)
    private UserProfile userProfile;

    public static String validateLogin(UserAccountRepository userAccountRepo,
                                       String username,
                                       String password)
    {
        UserAccount userAccount = userAccountRepo.findUserAccountByUsernameAndPassword(username, password)
                                                 .orElse(null);
        if (userAccount == null)
            throw new IllegalArgumentException("Invalid username or password.");
        if (!userAccount.isActive)
            throw new IllegalArgumentException("Account suspended.");
        String privilege = userAccount.userProfile.getPrivilege();
        return switch (privilege) {
            case "admin", "owner", "manager", "customer" ->
            {
                userAccount.timeLastLogin = OffsetDateTime.now();
                userAccountRepo.save(userAccount);
                yield privilege;
            }
            default -> throw new IllegalArgumentException("Invalid privilege.");
        };
    }

    public static void createUserAccount(UserAccountRepository userAccountRepo,
                                    UserProfileRepository userProfileRepo,
                                    final String username,
                                    final String password,
                                    final String email,
                                    final String firstName,
                                    final String lastName,
                                    final String address,
                                    final LocalDate dateOfBirth,
                                    final String title)
    {
        System.out.println("Method UserAccountImpl.createUserAccount() called.");
        // This validation is only required when an admin creates a new user account.
        UserProfile userProfile = userProfileRepo.findUserProfileByTitle(title).orElseThrow(
                () -> new IllegalArgumentException("Invalid title.")
        );
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

        UserAccount userAccount = new UserAccount();
        userAccount.setId(UUID.randomUUID());
        userAccount.setIsActive(true);
        userAccount.setTimeCreated(OffsetDateTime.now());
        userAccount.setTimeLastLogin(OffsetDateTime.now());
        userAccount.setUserProfile(userProfile);
        userAccount.setUsername(username.toLowerCase());
        userAccount.setPasswordHash(password); // Hashed in Postgres.
        userAccount.setEmail(email.toLowerCase());
        userAccount.setFirstName(firstName);
        userAccount.setLastName(lastName);
        userAccount.setAddress(address);
        userAccount.setDateOfBirth(dateOfBirth);
        userAccountRepo.save(userAccount);
    }
}