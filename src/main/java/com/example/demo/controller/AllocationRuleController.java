package com.example.demo.controller;

import com.example.demo.entity.AssetClassAllocationRule;
import com.example.demo.service.impl.AllocationRuleServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/allocation-rules")
public class AllocationRuleController {

    private final AllocationRuleServiceImpl allocationRuleService;

    public AllocationRuleController(AllocationRuleServiceImpl allocationRuleService) {
        this.allocationRuleService = allocationRuleService;
    }

    @PostMapping
    public ResponseEntity<AssetClassAllocationRule> createRule(@RequestBody AssetClassAllocationRule rule) {
        return ResponseEntity.ok(allocationRuleService.createRule(rule));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssetClassAllocationRule> updateRule(@PathVariable Long id,
                                                               @RequestBody AssetClassAllocationRule rule) {
        return ResponseEntity.ok(allocationRuleService.updateRule(id, rule));
    }

    @GetMapping("/investor/{investorId}")
    public ResponseEntity<List<AssetClassAllocationRule>> getRulesByInvestor(@PathVariable Long investorId) {
        return ResponseEntity.ok(allocationRuleService.getRulesByInvestor(investorId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetClassAllocationRule> getRuleById(@PathVariable Long id) {
        return ResponseEntity.ok(allocationRuleService.getRuleById(id));
    }

    @GetMapping
    public ResponseEntity<List<AssetClassAllocationRule>> getAllRules() {
        return ResponseEntity.ok(allocationRuleService.getAllRules());
    }
}
