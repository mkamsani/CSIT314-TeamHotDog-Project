package com.hotdog.ctbs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotdog.ctbs.repository.UserAccountRepository;
import jakarta.persistence.*;
import net.datafaker.Faker;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder; // Might not be needed.
import org.apache.commons.lang3.builder.ToStringStyle;             // Might not be needed.
import java.time.OffsetDateTime;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "account_type", nullable = false)
    private String accountType;

    @JsonIgnore
    @Column(name = "password_hash", nullable = false, length = 72)
    private String passwordHash;

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

    public UserAccount(UserAccount copy) {
        this.id = copy.id;
        this.username = copy.username;
        this.accountType = copy.accountType;
        this.passwordHash = copy.passwordHash;
        this.firstName = copy.firstName;
        this.lastName = copy.lastName;
        this.email = copy.email;
        this.address = copy.address;
        this.timeCreated = copy.timeCreated;
        this.timeLastLogin = copy.timeLastLogin;
    }

    /**
     * Returns a UserAccount object with random data.
     * @return UserAccount object with random data.
     */
    public static UserAccount fake(int currentIncrement) {
        Faker faker = new Faker();

        String fn = faker.name().firstName();
        // Use builder pattern to create a new UserAccount object
        return UserAccount.builder()
                          .id(currentIncrement)
                          .username(fn.toLowerCase() + faker.number().digits(2))
                          .accountType("customer")
                          .passwordHash(faker.number().digits(10))
                          .firstName(fn)
                          .lastName(faker.name().lastName())
                          .email(faker.internet().emailAddress())
                          .address(faker.address().streetAddress())
                          .timeCreated(OffsetDateTime.now())
                          .timeLastLogin(OffsetDateTime.now())
                          .build();
    }

    public static UserAccount fake() {
        return fake(1);
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

    /**
     * Returns a JSON string of the object. Backup implementation.
     */
    public String toApacheJSON() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}