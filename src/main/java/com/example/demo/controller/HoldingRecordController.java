package com.example.demo.controller;

import com.example.demo.entity.HoldingRecord;
import com.example.demo.entity.enums.AssetClassType; // Added missing import
import com.example.demo.service.HoldingRecordService; // Injecting Interface
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/holdings")
public class HoldingRecordController {

    // Best Practice: Use the interface 'HoldingRecordService' for injection
    private final HoldingRecordService holdingRecordService;

    public HoldingRecordController(HoldingRecordService holdingRecordService) {
        this.holdingRecordService = holdingRecordService;
    }

    @PostMapping
    public ResponseEntity<HoldingRecord> recordHolding(@RequestBody HoldingRecord holding) {
        return ResponseEntity.ok(holdingRecordService.recordHolding(holding));
    }

    @GetMapping("/investor/{investorId}")
    public ResponseEntity<List<HoldingRecord>> getHoldingsByInvestor(@PathVariable Long investorId) {
        return ResponseEntity.ok(holdingRecordService.getHoldingsByInvestor(investorId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<HoldingRecord>> getHoldingById(@PathVariable Long id) {
        return ResponseEntity.ok(holdingRecordService.getHoldingById(id));
    }

    @GetMapping
    public ResponseEntity<List<HoldingRecord>> getAllHoldings() {
        return ResponseEntity.ok(holdingRecordService.getAllHoldings());
    }

    @GetMapping("/investor/{investorId}/asset/{assetClass}")
    public ResponseEntity<List<HoldingRecord>> getHoldingsByInvestorAndAssetClass(
            @PathVariable Long investorId,
            @PathVariable AssetClassType assetClass) {
        return ResponseEntity.ok(
                holdingRecordService.getHoldingsByInvestorAndAssetClass(investorId, assetClass)
        );
    }
}