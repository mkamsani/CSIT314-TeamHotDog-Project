package com.hotdog.ctbs.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.*;
import lombok.*;
import net.datafaker.Faker;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * UserAccount <a href="https://www.baeldung.com/jpa-entities">entity</a>
 * <p>
 * The lombok package generates boilerplate code such as getters and
 * constructors, which we did manually in CSIT 111/121.
 * <p>
 * You can also use lombok to generate a {@code toString()} method.
 * If you don't want to use lombok, you can write the code yourself.
 * Instead of deleting the lombok annotations, use {@code @Override} in
 * places where you want to override a method.
 * <p>
 * An annotation is a special kind of comment that tells the compiler
 * to do something. For example, the {@code @Entity} annotation tells
 * the compiler that this class is an entity. If a lombok annotation
 * and an {@code @Override} annotation are both present, the
 * {@code @Override} annotation takes precedence.
 * <p>
 * The jackson package is used to convert Java objects to JSON. It is
 * used by Spring Boot to convert Java objects to JSON when a request
 * is made to a REST endpoint. The {@code @JsonIgnore} annotation tells
 * jackson to ignore a field when converting to JSON. See its use in
 * the {@code passwordHash} field, and the {@code toString()} method.
 * <p>
 * The jakarta.persistence package is used to map Java objects to a SQL
 * database. The {@code @Entity} annotation tells the compiler that the
 * class is an entity. The {@code @Id} annotation tells the compiler
 * that the field is the primary key. The {@code @GeneratedValue}
 * annotation tells the compiler that the primary key is automatically
 * generated. The {@code @Column} annotation tells the compiler that
 * the field is a column in the database.
 *
 * @author Baraq Kamsani
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "user_account")
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", nullable = false)
    private UUID id;

    @Column(name = "password_hash", nullable = false, length = 72)
    private String passwordHash;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_profile", nullable = false)
    private UserProfile userProfile;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "time_created", nullable = false)
    private OffsetDateTime timeCreated;

    @Column(name = "time_last_login", nullable = false)
    private OffsetDateTime timeLastLogin;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    /**
     * Returns a UserAccount object with random data.
     * @return UserAccount object with random data.
     */
    public static UserAccount fake(int currentIncrement) {
        Faker faker = new Faker();

        String fn = faker.name().firstName();
        // Use builder pattern to create a new UserAccount object
        return UserAccount.builder()
                          .id(UUID.randomUUID())
                          .passwordHash(faker.internet().password())
                          .userProfile(new UserProfile(faker))
                          .username(fn.toLowerCase() + currentIncrement)
                          .firstName(fn)
                          .lastName(faker.name().lastName())
                          .email(faker.internet().emailAddress())
                          .address(faker.address().fullAddress())
                          .timeCreated(OffsetDateTime.now())
                          .timeLastLogin(OffsetDateTime.now())
                          .dateOfBirth(faker.date().birthday().toInstant()
                                            .atOffset(OffsetDateTime.now().getOffset())
                                            .toLocalDate())
                          .build();
    }

    /**
     * Returns a JSON string of the object.
     * Register JavaTimeModule to support Java 8 date types.
     * @return JSON string of the object.
     */
    @SneakyThrows
    @Override
    public String toString() {
        return new ObjectMapper().registerModule(new JavaTimeModule())
                                 .writeValueAsString(this);
    }

}