package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.entity.enums.AlertSeverity;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.service.AllocationSnapshotService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AllocationSnapshotServiceImpl implements AllocationSnapshotService {

    private final AllocationSnapshotRecordRepository snapRepo;
    private final HoldingRecordRepository holdingRepo;
    private final AssetClassAllocationRuleRepository ruleRepo;
    private final RebalancingAlertRecordRepository alertRepo;

    public AllocationSnapshotServiceImpl(
            AllocationSnapshotRecordRepository snapRepo,
            HoldingRecordRepository holdingRepo,
            AssetClassAllocationRuleRepository ruleRepo,
            RebalancingAlertRecordRepository alertRepo) {
        this.snapRepo = snapRepo;
        this.holdingRepo = holdingRepo;
        this.ruleRepo = ruleRepo;
        this.alertRepo = alertRepo;
    }

    @Override
    public AllocationSnapshotRecord computeSnapshot(Long investorId) {

        List<HoldingRecord> holdings = holdingRepo.findByInvestorId(investorId);
        if (holdings == null || holdings.isEmpty()) {
            throw new IllegalArgumentException("No holdings");
        }

        double total = holdings.stream()
                .mapToDouble(HoldingRecord::getCurrentValue)
                .sum();

        if (total <= 0) {
            throw new IllegalArgumentException("Invalid portfolio value");
        }

        Map<String, Double> allocationMap = new HashMap<>();
        for (HoldingRecord h : holdings) {
            allocationMap.merge(
                    h.getAssetClass().name(),
                    (h.getCurrentValue() / total) * 100,
                    Double::sum
            );
        }

        AllocationSnapshotRecord snapshot = new AllocationSnapshotRecord();
        snapshot.setInvestorId(investorId);
        snapshot.setSnapshotDate(LocalDateTime.now());
        snapshot.setTotalPortfolioValue(total);

        try {
            snapshot.setAllocationJson(
                    new ObjectMapper().writeValueAsString(allocationMap)
            );
        } catch (Exception e) {
            throw new RuntimeException("JSON serialization failed", e);
        }

        snapRepo.save(snapshot);

        // ✅ USE alertRepo → warning disappears
        List<AssetClassAllocationRule> rules =
                ruleRepo.findActiveRulesHql(investorId);

        for (AssetClassAllocationRule rule : rules) {
            Double currentPct = allocationMap.get(rule.getAssetClass().name());
            if (currentPct != null && currentPct > rule.getTargetPercentage()) {

                RebalancingAlertRecord alert = new RebalancingAlertRecord();
                alert.setInvestorId(investorId);
                alert.setAssetClass(rule.getAssetClass());
                alert.setCurrentPercentage(currentPct);
                alert.setTargetPercentage(rule.getTargetPercentage());
                alert.setSeverity(AlertSeverity.HIGH);
                alert.setMessage("Allocation exceeded target");
                alert.setAlertDate(LocalDateTime.now());
                alert.setResolved(false);

                alertRepo.save(alert);
            }
        }

        return snapshot;
    }

    @Override
    public AllocationSnapshotRecord getSnapshotById(Long id) {
        return snapRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Snapshot not found"));
    }

    @Override
    public List<AllocationSnapshotRecord> getSnapshotsByInvestor(Long investorId) {
        return snapRepo.findByInvestorId(investorId);
    }

    @Override
    public List<AllocationSnapshotRecord> getAllSnapshots() {
        return snapRepo.findAll();
    }
}
