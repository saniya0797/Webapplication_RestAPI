package com.example.springboot;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "account")
public class User {
    @Column(name = "role")
    private String role;


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

//    @Column(name = "is_enabled")
//    public boolean isEnabled() {
//        return enabled;
//    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    private boolean enabled;
    @Id

    @Column(name = "Id", nullable = false)
    private String  id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String last_name;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "password")
    private String password;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "account_created", updatable = false)
    public LocalDateTime account_created;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "account_updated")
    public LocalDateTime account_updated;

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.last_name = lastName;
        this.email = email;
        this.password = password;

    }

    public LocalDateTime getAccount_created() {
        return account_created;
    }

    public void setAccount_created(LocalDateTime account_created) {
        this.account_created = account_created;
    }

    @Override
    public String toString() {
        return "EntityClass{" +
                "account_created=" + account_created +
                ", account_updated=" + account_updated +
                ", id='" + id + '\'' +
                ", first_name='" + firstName + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

  ;



    public LocalDateTime getAccount_updated() {
        return account_updated;
    }

    public void setAccount_updated(LocalDateTime account_updated) {
        this.account_updated = account_updated;
    }



    public String getId() {
        return id;
    }

    public void setId(String  id) {
        this.id = id;
    }


    public String getLast_name() {

        return last_name;
    }

    public void setLast_name(String last_name) {

        this.last_name = last_name;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public String getPassword() {

        return this.password;
    }

    public void setPassword(String password) {

        this.password = password;
    }



    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }


    public User() {
    }

    @PrePersist
    protected void onCreate() {
        this.account_created = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.account_updated = LocalDateTime.now();
    }


}