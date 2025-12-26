package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.entity.enums.AlertSeverity;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.service.AllocationSnapshotService;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class AllocationSnapshotServiceImpl implements AllocationSnapshotService {

    private final AllocationSnapshotRecordRepository snapshotRepository;
    private final HoldingRecordRepository holdingRepository;
    private final AssetClassAllocationRuleRepository allocationRuleRepository;
    private final RebalancingAlertRecordRepository alertRepository;

    public AllocationSnapshotServiceImpl(
            AllocationSnapshotRecordRepository snapshotRepository,
            HoldingRecordRepository holdingRepository,
            AssetClassAllocationRuleRepository allocationRuleRepository,
            RebalancingAlertRecordRepository alertRepository
    ) {
        this.snapshotRepository = snapshotRepository;
        this.holdingRepository = holdingRepository;
        this.allocationRuleRepository = allocationRuleRepository;
        this.alertRepository = alertRepository;
    }

    @Override
    public AllocationSnapshotRecord computeSnapshot(Long investorId) {
        List<HoldingRecord> holdings = holdingRepository.findByInvestorId(investorId);
        if (holdings.isEmpty()) {
            throw new IllegalArgumentException("No holdings for investor: " + investorId);
        }

        double totalValue = holdings.stream()
                .mapToDouble(HoldingRecord::getCurrentValue)
                .sum();

        if (totalValue <= 0) {
            throw new IllegalArgumentException("Total portfolio value must be > 0");
        }

        // Compute allocation percentages
        StringBuilder allocationJson = new StringBuilder("{");
        for (HoldingRecord h : holdings) {
            double pct = (h.getCurrentValue() / totalValue) * 100;
            allocationJson.append("\"").append(h.getAssetClass()).append("\": ").append(pct).append(",");
        }
        if (allocationJson.charAt(allocationJson.length() - 1) == ',') {
            allocationJson.deleteCharAt(allocationJson.length() - 1);
        }
        allocationJson.append("}");

        // Create snapshot record
        AllocationSnapshotRecord snapshot = new AllocationSnapshotRecord(
                investorId,
                LocalDateTime.now(),
                totalValue,
                allocationJson.toString()
        );
        snapshot = snapshotRepository.save(snapshot);

        // Compare with active rules and generate alerts
        List<AssetClassAllocationRule> rules = allocationRuleRepository.findByInvestorIdAndActiveTrue(investorId);
        for (AssetClassAllocationRule rule : rules) {
            holdings.stream()
                    .filter(h -> h.getAssetClass() == rule.getAssetClass())
                    .findFirst()
                    .ifPresent(h -> {
                        double currentPct = (h.getCurrentValue() / totalValue) * 100;
                        if (currentPct > rule.getTargetPercentage()) {
                            RebalancingAlertRecord alert = new RebalancingAlertRecord(
                                    investorId,
                                    rule.getAssetClass(),
                                    currentPct,
                                    rule.getTargetPercentage(),
                                    AlertSeverity.MEDIUM,
                                    "Asset allocation exceeded target",
                                    LocalDateTime.now(),
                                    false
                            );
                            alertRepository.save(alert);
                        }
                    });
        }

        return snapshot;
    }

    @Override
    public AllocationSnapshotRecord getSnapshotById(Long id) {
        return snapshotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Snapshot not found: " + id));
    }

    @Override
    public List<AllocationSnapshotRecord> getSnapshotsByInvestor(Long investorId) {
        return snapshotRepository.findByInvestorId(investorId);
    }

    @Override
    public List<AllocationSnapshotRecord> getAllSnapshots() {
        return snapshotRepository.findAll();
    }
}
