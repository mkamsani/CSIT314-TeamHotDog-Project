package com.hotdog.ctbs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "user_profile")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", nullable = false)
    private UUID id;

    @Column(name = "user_type", nullable = false)
    private String userType;

    @Column(name = "role_type")
    private String roleType;

    @OneToMany(mappedBy = "userProfile")
    private Set<UserAccount> userAccounts = new LinkedHashSet<>();

}