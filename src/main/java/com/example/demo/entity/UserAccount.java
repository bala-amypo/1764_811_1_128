package com.example.demo.entity;

import com.example.demo.entity.enums.RoleType;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
@Entity
public class UserAccount {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true)
    private String username;
    @Column(unique=true)
    @Email(message="Invalid email")
    private String email;
    private String password;
    private RoleType role;
    private Boolean active=true;
    public UserAccount() {
    }
    
    public UserAccount(Long id, String username, String email, String password, RoleType role, Boolean active) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.active = active;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public RoleType getRole() {
        return role;
    }
    public void setRole(RoleType role) {
        this.role = role;
    }
    public Boolean getActive() {
        return active;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }
}
