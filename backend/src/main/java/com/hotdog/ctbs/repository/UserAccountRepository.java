package com.hotdog.ctbs.repository;

import com.hotdog.ctbs.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {

    Optional<UserAccount> findUserAccountByUsername(final String username);

    Optional<UserAccount> findUserAccountByEmail(final String email);

    @Query(
            value = """
                    SELECT EXISTS
                    (
                        SELECT password_hash FROM user_account
                        WHERE  username      = ?1
                        AND    is_active     = TRUE
                        AND    password_hash = crypt(?2, password_hash)
                    )""",
            nativeQuery = true
    )
    boolean existsUserAccountByUsernameAndPassword(final String username, final String password);
}