package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.entity.*;
import com.example.demo.entity.enums.AlertSeverity;
import com.example.demo.entity.enums.AssetClassType;
import com.example.demo.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AllocationSnapshotServiceImpl {

    private final AllocationSnapshotRecordRepository snapshotRepo;
    private final HoldingRecordRepository holdingRepo;
    private final AssetClassAllocationRuleRepository ruleRepo;
    private final RebalancingAlertRecordRepository alertRepo;
    
    public AllocationSnapshotServiceImpl(
        AllocationSnapshotRecordRepository snapshotRepo,
        HoldingRecordRepository holdingRepo,
        AssetClassAllocationRuleRepository ruleRepo,
        RebalancingAlertRecordRepository alertRepo
    ) {
        this.snapshotRepo = snapshotRepo;
        this.holdingRepo = holdingRepo;
        this.ruleRepo = ruleRepo;
        this.alertRepo = alertRepo;
    }

    public AllocationSnapshotRecord computeSnapshot(Long investorId) throws Exception {

        List<HoldingRecord> holdings = holdingRepo.findByInvestorId(investorId);
        if (holdings.isEmpty()) {
            throw new IllegalArgumentException("No holdings");
        }

        double total = holdings.stream()
            .mapToDouble(HoldingRecord::getCurrentValue)
            .sum();

        if (total <= 0) {
            throw new IllegalArgumentException("totalPortfolioValue");
        }

        Map<AssetClassType, Double> allocation = new HashMap<>();
        for (HoldingRecord h : holdings) {
            allocation.merge(
                h.getAssetClass(),
                (h.getCurrentValue() / total) * 100,
                Double::sum
            );
        }

        AllocationSnapshotRecord snapshot = new AllocationSnapshotRecord();
        snapshot.setInvestorId(investorId);
        snapshot.setSnapshotDate(LocalDateTime.now());
        snapshot.setTotalportfolioValue(total);
        snapshot.setAllocationJson(new ObjectMapper().writeValueAsString(allocation));
        snapshotRepo.save(snapshot);

        List<AssetClassAllocationRule> rules = ruleRepo.findActiveRulesHql(investorId);

        for (AssetClassAllocationRule rule : rules) {
            Double currentPct = allocation.get(rule.getAssetClass());
            if (currentPct != null && currentPct > rule.getTargetPercentage()) {

                RebalancingAlertRecord alert = new RebalancingAlertRecord();
                alert.setInvestorId(investorId);
                alert.setAssetClass(rule.getAssetClass());
                alert.setCurrentPercentage(currentPct);
                alert.setTargetPercentage(rule.getTargetPercentage());
                alert.setSeverity(AlertSeverity.HIGH);
                alert.setMessage("Allocation exceeded target");
                alert.setAlertDate(LocalDateTime.now());
                alertRepo.save(alert);
            }
        }
        return snapshot;
    }
}

