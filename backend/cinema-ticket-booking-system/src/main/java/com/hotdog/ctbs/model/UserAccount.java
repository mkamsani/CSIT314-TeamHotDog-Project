package com.hotdog.ctbs.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "user_account")
public class UserAccount {
    @Id
    @Column(name = "id")
    private String userId;

    @Column(name = "username")
    private String username;

    @Column(name = "account_type")
    private String accountType;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "time_created")
    private OffsetDateTime timeCreated;

    @Column(name = "time_last_login")
    private OffsetDateTime timeLastLogin;

    public UserAccount() {
    }

    public UserAccount(String userId,
                       String username,
                       String accountType,
                       String passwordHash,
                       String firstName,
                       String lastName,
                       String email,
                       String address,
                       OffsetDateTime timeCreated,
                       OffsetDateTime timeLastLogin) {
        this.userId = userId;
        this.username = username;
        this.accountType = accountType;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.timeCreated = timeCreated;
        this.timeLastLogin = timeLastLogin;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public OffsetDateTime getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(OffsetDateTime timeCreated) {
        this.timeCreated = timeCreated;
    }

    public OffsetDateTime getTimeLastLogin() {
        return timeLastLogin;
    }

    public void setTimeLastLogin(OffsetDateTime timeLastLogin) {
        this.timeLastLogin = timeLastLogin;
    }
}
