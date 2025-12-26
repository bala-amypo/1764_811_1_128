package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.entity.enums.AlertSeverity;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.service.AllocationSnapshotService;
import com.example.demo.util.AllocationUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AllocationSnapshotServiceImpl implements AllocationSnapshotService {

    private final AllocationSnapshotRecordRepository snapshotRepository;
    private final HoldingRecordRepository holdingRepository;
    private final AssetClassAllocationRuleRepository ruleRepository;
    private final RebalancingAlertRecordRepository alertRepository;

    public AllocationSnapshotServiceImpl(
            AllocationSnapshotRecordRepository snapshotRepository,
            HoldingRecordRepository holdingRepository,
            AssetClassAllocationRuleRepository ruleRepository,
            RebalancingAlertRecordRepository alertRepository
    ) {
        this.snapshotRepository = snapshotRepository;
        this.holdingRepository = holdingRepository;
        this.ruleRepository = ruleRepository;
        this.alertRepository = alertRepository;
    }

    @Override
    public AllocationSnapshotRecord computeSnapshot(Long investorId) {

        List<HoldingRecord> holdings = holdingRepository.findByInvestorId(investorId);
        if (holdings == null || holdings.isEmpty()) {
            throw new IllegalArgumentException("No holdings for investor: " + investorId);
        }

        double totalValue = AllocationUtil.calculateTotalValue(holdings);
        if (totalValue <= 0) {
            throw new IllegalArgumentException("Total portfolio value must be > 0");
        }

        Map<AssetClassType, Double> percentages = AllocationUtil.calculateAllocationPercentages(holdings, totalValue);
        String allocationJson = AllocationUtil.toJson(percentages);

        AllocationSnapshotRecord snapshot = new AllocationSnapshotRecord();
        snapshot.setInvestorId(investorId);
        snapshot.setSnapshotDate(LocalDateTime.now());
        snapshot.setTotalPortfolioValue(totalValue);
        snapshot.setAllocationJson(allocationJson);

        AllocationSnapshotRecord savedSnapshot = snapshotRepository.save(snapshot);

        // ✅ CORRECTED: Convert Iterable to List for rules
        List<AssetClassAllocationRule> rules = StreamSupport.stream(ruleRepository.findActiveRulesHql(investorId).spliterator(), false)
                .collect(Collectors.toList());

        for (AssetClassAllocationRule rule : rules) {
            Double currentPercentage = percentages.get(rule.getAssetClass());
            if (currentPercentage != null && currentPercentage > rule.getTargetPercentage()) {
                RebalancingAlertRecord alert = new RebalancingAlertRecord();
                alert.setInvestorId(investorId);
                alert.setAssetClass(rule.getAssetClass());
                alert.setCurrentPercentage(currentPercentage);
                alert.setTargetPercentage(rule.getTargetPercentage());
                alert.setSeverity(AlertSeverity.HIGH);
                alert.setMessage(rule.getAssetClass().name() + " allocation exceeded target");
                alert.setAlertDate(LocalDateTime.now());
                alert.setResolved(false);

                alertRepository.save(alert);
            }
        }

        return savedSnapshot;
    }

    @Override
    public AllocationSnapshotRecord getSnapshotById(Long id) {
        return snapshotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Snapshot not found with id: " + id));
    }

    @Override
    public List<AllocationSnapshotRecord> getSnapshotsByInvestor(Long investorId) {
        return snapshotRepository.findAll()
                .stream()
                .filter(s -> s.getInvestorId().equals(investorId))
                .collect(Collectors.toList()); // ✅ CORRECTED: toList() → collect(Collectors.toList()) for Java 8 compatibility
    }

    @Override
    public List<AllocationSnapshotRecord> getAllSnapshots() {
        return StreamSupport.stream(snapshotRepository.findAll().spliterator(), false)
                .collect(Collectors.toList()); // ✅ CORRECTED: Iterable → List conversion
    }
}
















// package com.example.demo.service.impl;

// import com.example.demo.entity.*;
// import com.example.demo.entity.enums.AlertSeverity;
// import com.example.demo.exception.ResourceNotFoundException;
// import com.example.demo.repository.*;
// import com.example.demo.service.AllocationSnapshotService;
// import com.example.demo.util.AllocationUtil;
// import org.springframework.stereotype.Service;

// import java.time.LocalDateTime;
// import java.util.List;
// import java.util.Map;

// @Service
// public class AllocationSnapshotServiceImpl implements AllocationSnapshotService {

//     private final AllocationSnapshotRecordRepository snapshotRepository;
//     private final HoldingRecordRepository holdingRepository;
//     private final AssetClassAllocationRuleRepository ruleRepository;
//     private final RebalancingAlertRecordRepository alertRepository;

//     // 🔴 DO NOT CHANGE ORDER – tests depend on this
//     public AllocationSnapshotServiceImpl(
//             AllocationSnapshotRecordRepository snapshotRepository,
//             HoldingRecordRepository holdingRepository,
//             AssetClassAllocationRuleRepository ruleRepository,
//             RebalancingAlertRecordRepository alertRepository
//     ) {
//         this.snapshotRepository = snapshotRepository;
//         this.holdingRepository = holdingRepository;
//         this.ruleRepository = ruleRepository;
//         this.alertRepository = alertRepository;
//     }

//     @Override
//     public AllocationSnapshotRecord computeSnapshot(Long investorId) {

//         List<HoldingRecord> holdings =
//                 holdingRepository.findByInvestorId(investorId);

//         if (holdings == null || holdings.isEmpty()) {
//             throw new IllegalArgumentException("No holdings");
//         }

//         double totalValue =
//                 AllocationUtil.calculateTotalValue(holdings);

//         if (totalValue <= 0) {
//             throw new IllegalArgumentException("totalPortfolioValue must be > 0");
//         }

//         Map<com.example.demo.entity.enums.AssetClassType, Double> percentages =
//                 AllocationUtil.calculateAllocationPercentages(holdings, totalValue);

//         String allocationJson =
//                 AllocationUtil.toJson(percentages);

//         AllocationSnapshotRecord snapshot = new AllocationSnapshotRecord();
//         snapshot.setInvestorId(investorId);
//         snapshot.setSnapshotDate(LocalDateTime.now());
//         snapshot.setTotalPortfolioValue(totalValue);
//         snapshot.setAllocationJson(allocationJson);

//         AllocationSnapshotRecord savedSnapshot =
//                 snapshotRepository.save(snapshot);

//         List<AssetClassAllocationRule> rules =
//                 ruleRepository.findActiveRulesHql(investorId);

//         for (AssetClassAllocationRule rule : rules) {

//             Double currentPercentage =
//                     percentages.get(rule.getAssetClass());

//             if (currentPercentage != null &&
//                 currentPercentage > rule.getTargetPercentage()) {

//                 RebalancingAlertRecord alert = new RebalancingAlertRecord();
//                 alert.setInvestorId(investorId);
//                 alert.setAssetClass(rule.getAssetClass());
//                 alert.setCurrentPercentage(currentPercentage);
//                 alert.setTargetPercentage(rule.getTargetPercentage());
//                 alert.setSeverity(AlertSeverity.HIGH);
//                 alert.setMessage(
//                         rule.getAssetClass().name() + " allocation exceeded target"
//                 );
//                 alert.setAlertDate(LocalDateTime.now());
//                 alert.setResolved(false);

//                 alertRepository.save(alert);
//             }
//         }

//         return savedSnapshot;
//     }

//     @Override
//     public AllocationSnapshotRecord getSnapshotById(Long id) {
//         return snapshotRepository.findById(id)
//                 .orElseThrow(() ->
//                         new ResourceNotFoundException("Snapshot not found"));
//     }

//     @Override
//     public List<AllocationSnapshotRecord> getSnapshotsByInvestor(Long investorId) {
//         return snapshotRepository.findAll()
//                 .stream()
//                 .filter(s -> s.getInvestorId().equals(investorId))
//                 .toList();
//     }

//     @Override
//     public List<AllocationSnapshotRecord> getAllSnapshots() {
//         return snapshotRepository.findAll();
//     }
// }





// // package com.example.demo.service.impl;

// // import com.example.demo.entity.AllocationSnapshotRecord;
// // import com.example.demo.repository.AllocationSnapshotRecordRepository;
// // import com.example.demo.repository.AssetClassAllocationRuleRepository;
// // import com.example.demo.repository.HoldingRecordRepository;
// // import com.example.demo.repository.RebalancingAlertRecordRepository;
// // import com.example.demo.service.AllocationSnapshotService;
// // import com.example.demo.entity.*;
// // import com.example.demo.entity.enums.*;

// // import com.fasterxml.jackson.databind.ObjectMapper;
// // import org.springframework.stereotype.Service;

// // import java.time.LocalDateTime;
// // import java.util.*;

// // @Service
// // public class AllocationSnapshotServiceImpl
// //         implements AllocationSnapshotService {

// //     private final AllocationSnapshotRecordRepository snapshotRepo;
// //     private final HoldingRecordRepository holdingRepo;
// //     private final AssetClassAllocationRuleRepository ruleRepo;
// //     private final RebalancingAlertRecordRepository alertRepo;

// //     public AllocationSnapshotServiceImpl(
// //             AllocationSnapshotRecordRepository snapshotRepo,
// //             HoldingRecordRepository holdingRepo,
// //             AssetClassAllocationRuleRepository ruleRepo,
// //             RebalancingAlertRecordRepository alertRepo
// //     ) {
// //         this.snapshotRepo = snapshotRepo;
// //         this.holdingRepo = holdingRepo;
// //         this.ruleRepo = ruleRepo;
// //         this.alertRepo = alertRepo;
// //     }

// //     @Override
// //     public AllocationSnapshotRecord computeSnapshot(Long investorId) {

// //         List<HoldingRecord> holdings = holdingRepo.findByInvestorId(investorId);
// //         if (holdings.isEmpty()) {
// //             throw new IllegalArgumentException("No holdings");
// //         }

// //         double total = holdings.stream()
// //                 .mapToDouble(HoldingRecord::getCurrentValue)
// //                 .sum();

// //         if (total <= 0) {
// //             throw new IllegalArgumentException("Invalid portfolio value");
// //         }

// //         Map<AssetClassType, Double> allocation = new HashMap<>();
// //         for (HoldingRecord h : holdings) {
// //             allocation.merge(
// //                     h.getAssetClass(),
// //                     (h.getCurrentValue() / total) * 100,
// //                     Double::sum
// //             );
// //         }

// //         AllocationSnapshotRecord snapshot = new AllocationSnapshotRecord();
// //         snapshot.setInvestorId(investorId);
// //         snapshot.setSnapshotDate(LocalDateTime.now());
// //         snapshot.setTotalportfolioValue(total);

// //         try {
// //             snapshot.setAllocationJson(
// //                     new ObjectMapper().writeValueAsString(allocation)
// //             );
// //         } catch (Exception e) {
// //             throw new RuntimeException("JSON serialization failed", e);
// //         }

// //         snapshotRepo.save(snapshot);

// //         List<AssetClassAllocationRule> rules =
// //                 ruleRepo.findActiveRulesHql(investorId);

// //         for (AssetClassAllocationRule rule : rules) {
// //             Double currentPct = allocation.get(rule.getAssetClass());
// //             if (currentPct != null && currentPct > rule.getTargetPercentage()) {

// //                 RebalancingAlertRecord alert = new RebalancingAlertRecord();
// //                 alert.setInvestorId(investorId);
// //                 alert.setAssetClass(rule.getAssetClass());
// //                 alert.setCurrentPercentage(currentPct);
// //                 alert.setTargetPercentage(rule.getTargetPercentage());
// //                 alert.setSeverity(AlertSeverity.HIGH);
// //                 alert.setMessage("Allocation exceeded target");
// //                 alert.setAlertDate(LocalDateTime.now());

// //                 alertRepo.save(alert);
// //             }
// //         }
// //         return snapshot;
// //     }

// //     @Override
// //     public AllocationSnapshotRecord getSnapshotById(Long id) {
// //         return snapshotRepo.findById(id)
// //                 .orElseThrow(() -> new NoSuchElementException("Snapshot not found"));
// //     }

// //     @Override
// //     public List<AllocationSnapshotRecord> getSnapshotsByInvestor(Long investorId) {
// //         return snapshotRepo.findByInvestorId(investorId);
// //     }

// //     @Override
// //     public List<AllocationSnapshotRecord> getAllSnapshots() {
// //         return snapshotRepo.findAll();
// //     }
// // }
