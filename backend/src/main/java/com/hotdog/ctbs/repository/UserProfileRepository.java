package com.hotdog.ctbs.repository;

import com.hotdog.ctbs.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    Optional<List<UserProfile>> findUserProfilesByTitle(String s);

    Optional<List<UserProfile>> findUserProfilesByPrivilege(String privilege);

    Optional<UserProfile> findUserProfileByTitle(String s);
}