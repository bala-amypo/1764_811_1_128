package com.example.demo.entity;

import com.example.demo.entity.enums.AssetClassType;
import jakarta.persistence.*;

@Entity
public class AssetClassAllocationRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long investorId;

    @Enumerated(EnumType.STRING)
    private AssetClassType assetClass;

    private Double targetPercentage;

    private Boolean active;

    public AssetClassAllocationRule() {}

    public AssetClassAllocationRule(Long investorId, AssetClassType assetClass, Double targetPercentage, Boolean active) {
        this.investorId = investorId;
        this.assetClass = assetClass;
        this.targetPercentage = targetPercentage;
        this.active = active;
    }

    public void validatePercentage() {
        if (targetPercentage == null || targetPercentage < 0 || targetPercentage > 100) {
            throw new IllegalArgumentException("between 0 and 100");
        }
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getInvestorId() { return investorId; }
    public void setInvestorId(Long investorId) { this.investorId = investorId; }
    public AssetClassType getAssetClass() { return assetClass; }
    public void setAssetClass(AssetClassType assetClass) { this.assetClass = assetClass; }
    public Double getTargetPercentage() { return targetPercentage; }
    public void setTargetPercentage(Double targetPercentage) { this.targetPercentage = targetPercentage; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
