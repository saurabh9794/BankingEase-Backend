package com.bankingease.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String phone;

    private String role;


    // Default constructor
    public User() {
    }

    // Parameterized constructor
    public User(String name,
                String email,
                String password,
                String phone) {

        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    // Get ID
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Get Name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Get Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Get Password
    public String getPassword() {
        return password;
    }

    // Set Password
    public void setPassword(String password) {
        this.password = password;
    }

    // Get Phone
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}