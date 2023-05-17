package com.hotdog.ctbs.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotdog.ctbs.repository.UserAccountRepository;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;
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

    public static String login(UserAccountRepository userAccountRepo,
                               String username,
                               String password)
    {
        UserAccount userAccount = userAccountRepo.findUserAccountByUsernameAndPassword(username, password).orElse(null);
        if (userAccount == null)
            throw new IllegalArgumentException("Invalid username or password.");
        if (!userAccount.isActive)
            throw new IllegalArgumentException("Account suspended.");
        return switch (userAccount.userProfile.getPrivilege()) {
            case "admin" -> {
                userAccount.timeLastLogin = OffsetDateTime.now();
                userAccountRepo.save(userAccount);
                yield "admin";
            }
            case "owner" -> {
                userAccount.timeLastLogin = OffsetDateTime.now();
                userAccountRepo.save(userAccount);
                yield "owner";
            }
            case "manager" -> {
                userAccount.timeLastLogin = OffsetDateTime.now();
                userAccountRepo.save(userAccount);
                yield "manager";
            }
            case "customer" -> {
                userAccount.timeLastLogin = OffsetDateTime.now();
                userAccountRepo.save(userAccount);
                yield "customer";
            }
            default -> throw new IllegalArgumentException("Invalid privilege.");
        };
    }
}