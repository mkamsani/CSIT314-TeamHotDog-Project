package com.hotdog.ctbs.repository;

import com.hotdog.ctbs.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    Optional<UserProfile> findUserProfileByTitle(String s);
    List<UserProfile> findUserProfilesByPrivilege(String privilege);
    List<UserProfile> findUserProfilesByIsActiveTrue();
    @Query(value = "SELECT title FROM user_profile\n", nativeQuery = true)
    List<String> findAllTitles();
    @Query(value = "SELECT DISTINCT privilege FROM user_profile\n", nativeQuery = true)
    List<String> findAllPrivileges();
}