package com.example.demo.entity;

import com.example.demo.entity.enums.AlertSeverity;
import com.example.demo.entity.enums.AssetClassType;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
public class RebalancingAlertRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long investorId;

    @Enumerated(EnumType.STRING)
    private AssetClassType assetClass;

    private Double currentPercentage;

    private Double targetPercentage;

    @Enumerated(EnumType.STRING)
    private AlertSeverity severity;

    private String message;

    private LocalDateTime alertDate = LocalDateTime.now();

    private Boolean resolved = false;

    public RebalancingAlertRecord() {}

    public RebalancingAlertRecord(Long investorId, AssetClassType assetClass, Double currentPercentage, Double targetPercentage,
                                  AlertSeverity severity, String message, LocalDateTime alertDate, Boolean resolved) {
        this.investorId = investorId;
        this.assetClass = assetClass;
        this.currentPercentage = currentPercentage;
        this.targetPercentage = targetPercentage;
        this.severity = severity;
        this.message = message;
        this.alertDate = alertDate;
        this.resolved = resolved;
    }

    public void validatePercentages() {
        if (currentPercentage == null || targetPercentage == null || currentPercentage <= targetPercentage) {
            throw new IllegalArgumentException("currentPercentage > targetPercentage");
        }
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getInvestorId() { return investorId; }
    public void setInvestorId(Long investorId) { this.investorId = investorId; }
    public AssetClassType getAssetClass() { return assetClass; }
    public void setAssetClass(AssetClassType assetClass) { this.assetClass = assetClass; }
    public Double getCurrentPercentage() { return currentPercentage; }
    public void setCurrentPercentage(Double currentPercentage) { this.currentPercentage = currentPercentage; }
    public Double getTargetPercentage() { return targetPercentage; }
    public void setTargetPercentage(Double targetPercentage) { this.targetPercentage = targetPercentage; }
    public AlertSeverity getSeverity() { return severity; }
    public void setSeverity(AlertSeverity severity) { this.severity = severity; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public LocalDateTime getAlertDate() { return alertDate; }
    public void setAlertDate(LocalDateTime alertDate) { this.alertDate = alertDate; }
    public Boolean getResolved() { return resolved; }
    public void setResolved(Boolean resolved) { this.resolved = resolved; }
}
