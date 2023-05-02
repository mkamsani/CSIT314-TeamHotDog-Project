package com.hotdog.ctbs.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_profile")
@JsonIgnoreProperties({"userAccounts"})
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", nullable = false)
    private UUID id;

    @Column(name = "privilege", nullable = false)
    private String privilege;

    @Column(name = "title", nullable = false)
    private String title;

    @OneToMany(mappedBy = "userProfile")
    private Set<UserAccount> userAccounts = new LinkedHashSet<>();

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    /**
     * @return JSON string of the user profile.
     */
    @SneakyThrows
    @Override
    public String toString()
    {
        return new ObjectMapper().writeValueAsString(this);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof UserProfile that)) return false;
        // Prevent duplicates like "Senior Admin" and "senior admin".
        return id.equals(that.id) && title.equalsIgnoreCase(that.title);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, title);
    }
}