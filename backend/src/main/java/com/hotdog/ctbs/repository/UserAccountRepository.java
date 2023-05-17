package com.hotdog.ctbs.repository;

import com.hotdog.ctbs.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {

    Optional<UserAccount> findUserAccountByUsername(final String username);

    Optional<UserAccount> findUserAccountByEmail(final String email);

    List<UserAccount> findUserAccountsByUserProfile_Privilege(final String privilege);

    List<UserAccount> findUserAccountsByIsActiveTrue();

    @Query(value = """
            SELECT * FROM user_account
            WHERE  username      = ?1
            AND    password_hash = crypt(?2, password_hash)
            """, nativeQuery = true
    )
    Optional<UserAccount> findUserAccountByUsernameAndPassword(final String username, final String password);
}