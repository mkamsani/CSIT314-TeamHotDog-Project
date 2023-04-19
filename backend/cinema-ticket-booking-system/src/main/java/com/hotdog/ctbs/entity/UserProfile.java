package com.hotdog.ctbs.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.*;
import lombok.*;
import net.datafaker.Faker;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_profile")
public class UserProfile {
    @Id
    @Column(name = "uuid", nullable = false)
    private UUID id;

    @Column(name = "privilege", nullable = false)
    private String privilege;

    @Column(name = "title", nullable = false)
    private String title;

    @OneToMany(mappedBy = "userProfile")
    private Set<UserAccount> userAccounts = new LinkedHashSet<>();

    public UserProfile(String privilege, String title) {
        this.id = UUID.randomUUID();
        this.privilege = privilege;
        this.title = title;
    }

    public UserProfile(Faker faker) {
        this.id = UUID.randomUUID();
        this.privilege = switch (faker.random().nextInt(4)) {
            case 0 -> "customer";
            case 1 -> "manager";
            case 2 -> "owner";
            default -> "admin";
        };
        this.title = this.privilege.equals("customer")
                ? "Customer"
                : faker.name().title();
    }

    public UserProfile(UserProfile userProfile) {
        this.id = userProfile.id;
        this.privilege = userProfile.privilege;
        this.title = userProfile.title;
    }

    @SneakyThrows
    public UserProfile(String json) {
        UserProfile userProfile = new ObjectMapper().registerModule(new JavaTimeModule())
                                                    .readValue(json, UserProfile.class);
        this.id = userProfile.id;
        this.privilege = userProfile.privilege;
        this.title = userProfile.title;
    }

    /**
     * Returns a JSON string of the object.
     *
     * <p>The purpose of registerModule() is to support for Java 8 date types,<br />
     * as {@code java.time.OffsetDateTime} corresponds to Postgres' {@code timestamptz}.
     *
     * @return JSON string of the object.
     * @see com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
     * @see java.time.OffsetDateTime
     */
    @SneakyThrows
    @Override
    public String toString() {
        return new ObjectMapper().registerModule(new JavaTimeModule())
                                 .writerWithDefaultPrettyPrinter()
                                 .writeValueAsString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        if (!id.equals(that.id)) return false;
        return privilege.equals(that.privilege) && title.equals(that.title);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}