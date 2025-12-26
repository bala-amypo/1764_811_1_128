package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.entity.enums.*;
import com.example.demo.repository.*;
import com.example.demo.service.AllocationSnapshotService;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AllocationSnapshotServiceImpl implements AllocationSnapshotService {

    private final AllocationSnapshotRecordRepository snapshotRepo;
    private final HoldingRecordRepository holdingRepo;
    private final AssetClassAllocationRuleRepository ruleRepo;
    private final RebalancingAlertRecordRepository alertRepo;

    public AllocationSnapshotServiceImpl(
            AllocationSnapshotRecordRepository snapshotRepo,
            HoldingRecordRepository holdingRepo,
            AssetClassAllocationRuleRepository ruleRepo,
            RebalancingAlertRecordRepository alertRepo) {
        this.snapshotRepo = snapshotRepo;
        this.holdingRepo = holdingRepo;
        this.ruleRepo = ruleRepo;
        this.alertRepo = alertRepo;
    }

    @Override
    public AllocationSnapshotRecord computeSnapshot(Long investorId) {

        List<HoldingRecord> holdings = holdingRepo.findByInvestorId(investorId);
        if (holdings.isEmpty()) {
            throw new IllegalArgumentException("No holdings");
        }

        double total = holdings.stream()
                .mapToDouble(HoldingRecord::getCurrentValue)
                .sum();

        Map<AssetClassType, Double> percentages = new HashMap<>();

        for (HoldingRecord h : holdings) {
            percentages.merge(
                h.getAssetClass(),
                (h.getCurrentValue() / total) * 100,
                Double::sum
            );
        }

        AllocationSnapshotRecord snapshot = new AllocationSnapshotRecord();
        snapshot.setInvestorId(investorId);
        snapshot.setTotalPortfolioValue(total);

        try {
            snapshot.setAllocationJson(
                new ObjectMapper().writeValueAsString(percentages)
            );
        } catch (Exception e) {
            snapshot.setAllocationJson("{}");
        }

        snapshotRepo.save(snapshot);

        for (AssetClassAllocationRule rule : ruleRepo.findActiveRulesHql(investorId)) {
            double current = percentages.getOrDefault(rule.getAssetClass(), 0.0);
            if (current > rule.getTargetPercentage()) {

                RebalancingAlertRecord alert = new RebalancingAlertRecord();
                alert.setInvestorId(investorId);
                alert.setAssetClass(rule.getAssetClass());
                alert.setCurrentPercentage(current);
                alert.setTargetPercentage(rule.getTargetPercentage());
                alert.setSeverity(AlertSeverity.HIGH);
                alert.setMessage("Rebalancing required");

                alert.validate();
                alertRepo.save(alert);
            }
        }

        return snapshot;
    }
}
