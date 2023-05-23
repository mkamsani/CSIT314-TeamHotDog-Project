package com.hotdog.ctbs.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotdog.ctbs.repository.UserAccountRepository;
import com.hotdog.ctbs.repository.UserProfileRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
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
    protected UUID id;

    @Column(name = "is_active", nullable = false)
    protected Boolean isActive;

    @Column(name = "password_hash", nullable = false, length = 72)
    protected String passwordHash;

    @Column(name = "username", nullable = false)
    protected String username;

    @Column(name = "email", nullable = false)
    protected String email;

    @Column(name = "first_name", nullable = false)
    protected String firstName;

    @Column(name = "last_name", nullable = false)
    protected String lastName;

    @Column(name = "address", nullable = false)
    protected String address;

    @Column(name = "date_of_birth", nullable = false)
    protected LocalDate dateOfBirth;

    @Column(name = "time_created", nullable = false)
    protected OffsetDateTime timeCreated;

    @Column(name = "time_last_login", nullable = false)
    protected OffsetDateTime timeLastLogin;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_profile", nullable = false)
    @Fetch(FetchMode.JOIN)
    protected UserProfile userProfile;

    @Transient
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    //////////////////////////////// Service /////////////////////////////////

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
            case "admin", "owner", "manager", "customer" -> {
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
        // This validation is only required when an admin creates a new user account.
        UserProfile userProfile = userProfileRepo.findUserProfileByTitle(title).orElse(null);
        if (userProfile == null)
            throw new IllegalArgumentException("Invalid title: " + title);
        // The following validations are required when an admin/customer creates a new user account.
        if (userAccountRepo.findUserAccountByUsername(username).isPresent())
            throw new IllegalArgumentException("Username already exists: " + username);
        if (userAccountRepo.findUserAccountByEmail(email).isPresent())
            throw new IllegalArgumentException("Email already exists: " + email);
        if (!username.matches("[a-zA-Z0-9]+"))
            throw new IllegalArgumentException("Username must be alphanumeric: " + username);
        if (username.equals("admin") || username.equals("customer") ||
            username.equals("owner") || username.equals("manager"))
            throw new IllegalArgumentException("Username is reserved: " + username);
        if (dateOfBirth.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Invalid date of birth: " + dateOfBirth);


        UserAccount userAccount = new UserAccount();
        userAccount.id = UUID.randomUUID();
        userAccount.isActive = true;
        userAccount.timeCreated = OffsetDateTime.now();
        userAccount.timeLastLogin = OffsetDateTime.now();
        userAccount.userProfile = userProfile;
        userAccount.username = username.toLowerCase();
        userAccount.passwordHash = password;
        userAccount.email = email.toLowerCase();
        userAccount.firstName = firstName;
        userAccount.lastName = lastName;
        userAccount.address = address;
        userAccount.dateOfBirth = dateOfBirth;
        userAccountRepo.save(userAccount);
    }

    public static String readUserAccount(UserAccountRepository userAccountRepo,
                                         String param)
    {
        List<UserAccount> uaList = switch (param) {
            case "admin", "owner", "manager", "customer" ->
                    userAccountRepo.findUserAccountsByUserProfile_Privilege(param);
            case "active" -> userAccountRepo.findUserAccountsByIsActiveTrue();
            case "all" -> userAccountRepo.findAll();
            default -> {
                UserAccount userAccount = userAccountRepo.findUserAccountByUsername(param)
                                                         .orElse(null);
                if (userAccount == null)
                    throw new IllegalArgumentException("Invalid username.");
                yield List.of(userAccount);
            }
        };

        ArrayNode an = objectMapper.createArrayNode();
        for (UserAccount ua : uaList) {
            ObjectNode on = objectMapper.createObjectNode();
            on.put("username", ua.username);
            on.put("email", ua.email);
            on.put("firstName", ua.firstName);
            on.put("lastName", ua.lastName);
            on.put("dateOfBirth", ua.dateOfBirth.toString());
            on.put("address", ua.address);
            on.put("isActive", ua.isActive.toString());
            on.put("timeCreated", ua.timeCreated.toString());
            on.put("timeLastLogin", ua.timeLastLogin.toString());
            on.put("title", ua.userProfile.title);
            on.put("privilege", ua.userProfile.privilege);
            an.add(on);
        }
        return an.toString();
    }

    public static void updateUserAccount(final UserAccountRepository userAccountRepo,
                                         final UserProfileRepository userProfileRepo,
                                         final String targetUsername,
                                         final String username,
                                         final String firstName,
                                         final String lastName,
                                         final String email,
                                         final String address,
                                         final LocalDate dateOfBirth,
                                         final String title)
    {
        UserAccount userAccount = userAccountRepo.findUserAccountByUsername(targetUsername).orElse(null);
        if (userAccount == null)
            throw new IllegalArgumentException("Invalid username: " + targetUsername);
        UserProfile userProfile = userProfileRepo.findUserProfileByTitle(title).orElse(null);
        if (userProfile == null)
            throw new IllegalArgumentException("Invalid title: " + title);

        if (userAccountRepo.findUserAccountByUsername(username).isPresent() &&
            !userAccount.username.equals(username))
            throw new IllegalArgumentException("Username already exists: " + username);
        if (userAccountRepo.findUserAccountByEmail(email).isPresent() &&
            !userAccount.email.equals(email))
            throw new IllegalArgumentException("Email already exists: " + email);
        if (!username.matches("[a-zA-Z0-9]+"))
            throw new IllegalArgumentException("Username must be alphanumeric: " + username);
        if (username.equals("admin") || username.equals("customer") ||
            username.equals("owner") || username.equals("manager"))
            throw new IllegalArgumentException("Username is reserved: " + username);
        if (dateOfBirth.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Invalid date of birth: " + dateOfBirth);

        userAccount.username = username.toLowerCase();
        userAccount.email = email.toLowerCase();
        userAccount.firstName = firstName;
        userAccount.lastName = lastName;
        userAccount.address = address;
        userAccount.dateOfBirth = dateOfBirth;
        userAccount.userProfile = userProfile;
        userAccountRepo.save(userAccount);
    }

    public static void suspendUserAccount(UserAccountRepository userAccountRepo,
                                          String targetUsername)
    {
        UserAccount userAccount = userAccountRepo.findUserAccountByUsername(targetUsername).orElse(null);
        if (userAccount == null)
            throw new IllegalArgumentException("Invalid username: " + targetUsername);
        if (!userAccount.getIsActive())
            throw new IllegalArgumentException("User account is already suspended: " + targetUsername);
        userAccount.isActive = false;
        userAccountRepo.save(userAccount);
    }
}