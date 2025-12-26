package com.example.demo.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
public class InvestorProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String investorId;

    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    private Boolean active = true;

    private LocalDateTime createdAt = LocalDateTime.now();

    public InvestorProfile() {}

    public InvestorProfile(String investorId, String fullName, String email, Boolean active) {
        this.investorId = investorId;
        this.fullName = fullName;
        this.email = email;
        this.active = active;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getInvestorId() { return investorId; }
    public void setInvestorId(String investorId) { this.investorId = investorId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
