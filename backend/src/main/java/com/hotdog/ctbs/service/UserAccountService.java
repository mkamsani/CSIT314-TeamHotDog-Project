package com.hotdog.ctbs.service;

import com.hotdog.ctbs.entity.UserAccount;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 */
public interface UserAccountService {
    @Transactional
    String login(String username,
                 String password);

    void create(String username,
                String password,
                String email,
                String firstName,
                String lastName,
                String address,
                LocalDate dateOfBirth,
                String title);

    @Transactional
    void update(String target,
                String username,
                String firstName,
                String lastName,
                String email,
                String address,
                LocalDate dateOfBirth,
                String title);

    @Transactional
    void suspend(String target);

    @Transactional
    UserAccount getUserAccountByUsername(String username);

    List<UserAccount> getAllUserAccounts();

    List<UserAccount> getActiveUserAccounts();

    @Transactional
    List<UserAccount> getUserAccountsByPrivilege(String privilege);
}
