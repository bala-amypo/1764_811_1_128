package com.example.demo.entity;

import java.time.LocalDateTime;

import com.example.demo.entity.enums.AssetClassType;

import jakarta.persistence.*;

@Entity
public class HoldingRecord {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private Long investorId;
    private AssetClassType assetClass;
    private Double currentValue;
    private LocalDateTime snapshotDate = LocalDateTime.now();
    public HoldingRecord() {
    }
    
    public HoldingRecord(Long id, Long investorId, AssetClassType assetClass, Double currentValue,
            LocalDateTime snapshotDate) {
        this.id = id;
        this.investorId = investorId;
        this.assetClass = assetClass;
        this.currentValue = currentValue;
        this.snapshotDate = snapshotDate;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getInvestorId() {
        return investorId;
    }
    public void setInvestorId(Long investorId) {
        this.investorId = investorId;
    }
    public AssetClassType getAssetClass() {
        return assetClass;
    }
    public void setAssetClass(AssetClassType assetClass) {
        this.assetClass = assetClass;
    }
    public Double getCurrentValue() {
        return currentValue;
    }
    public void setCurrentValue(Double currentValue) {
        this.currentValue = currentValue;
    }
    public LocalDateTime getSnapshotDate() {
        return snapshotDate;
    }
    public void setSnapshotDate(LocalDateTime snapshotDate) {
        this.snapshotDate = snapshotDate;
    } 
    
}
