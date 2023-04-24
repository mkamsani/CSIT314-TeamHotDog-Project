package com.hotdog.ctbs.entity;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_account")
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_profile", nullable = false)
    private UserProfile userProfile;

    @Column(name = "is_active")
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

    /**
     * Returns a JSON string of the user account.
     *
     * <p>To support Java 8 date types,
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
    public String toString() {
        return new ObjectMapper().registerModule(new JavaTimeModule())
                                 .writeValueAsString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount that)) return false;
        return id.equals(that.id) && username.equals(that.username) && email.equals(that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email);
    }
}