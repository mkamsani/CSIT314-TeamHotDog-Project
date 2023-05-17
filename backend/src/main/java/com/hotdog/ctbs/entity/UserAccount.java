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
import org.springframework.stereotype.Repository;

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

    /**
     * Returns a JSON string of the object.
     * <p>
     * To support Java 8 date types,
     * {@link com.fasterxml.jackson.databind.ObjectMapper#registerModule(Module)} is used.<br />
     * {@link java.time.OffsetDateTime} corresponds to Postgres'
     * {@code TIMESTAMP WITH TIME ZONE}.
     *
     * @return JSON string of the user account.
     * @see <a href="https://www.baeldung.com/jpa-java-time#after-java-8">
     * Baeldung: Mapping Java 8 Date Types
     * </a>
     * @see <a href="https://www.baeldung.com/jackson-serialize-dates#java-8">
     * Baeldung: Serialize Java 8 Date With Jackson
     * </a>
     */
    @SneakyThrows
    @Override
    public String toString()
    {
        return new ObjectMapper().registerModule(new JavaTimeModule())
                                 .writeValueAsString(this);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof UserAccount that)) return false;
        return id.equals(that.id) &&
               username.equalsIgnoreCase(that.username) &&
               email.equalsIgnoreCase(that.email);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, username, email);
    }

    public String privilege()
    {
        return userProfile.getPrivilege();
    }

    public static String login(UserAccountRepository userAccountRepo, String username, String password)
    {
        UserAccount userAccount = userAccountRepo.findUserAccountByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("Invalid username or password.")
        );
        if (!userAccountRepo.existsUserAccountByUsernameAndPassword(username, password))
            throw new IllegalArgumentException("Invalid username or password.");
        if (!userAccount.isActive) throw new IllegalArgumentException("Account suspended.");
        String privilege = userAccount.privilege();
        if (privilege == null) throw new IllegalArgumentException("Invalid privilege.");
        if (!privilege.equals("admin") && !privilege.equals("customer") &&
            !privilege.equals("owner") && !privilege.equals("manager"))
            throw new IllegalArgumentException("Invalid privilege.");
        userAccountRepo.save(userAccount);
        userAccount.timeLastLogin = OffsetDateTime.now();

        return privilege;
    }
}