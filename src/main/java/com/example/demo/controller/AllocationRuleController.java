package com.example.demo.controller;

import com.example.demo.entity.AssetClassAllocationRule;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.AllocationRuleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/allocation-rules")
public class AllocationRuleController {

    private final AllocationRuleService service;

    public AllocationRuleController(AllocationRuleService service) {
        this.service = service;
    }

    // ---------------- CREATE ----------------
    @PostMapping
    public ResponseEntity<AssetClassAllocationRule> create(@RequestBody AssetClassAllocationRule rule) {
        try {
            AssetClassAllocationRule created = service.createRule(rule);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build(); // invalid targetPercentage
        }
    }

    // ---------------- UPDATE ----------------
    @PutMapping("/{id}")
    public ResponseEntity<AssetClassAllocationRule> update(@PathVariable Long id,
                                                           @RequestBody AssetClassAllocationRule rule) {
        try {
            AssetClassAllocationRule updated = service.updateRule(id, rule);
            return ResponseEntity.ok(updated);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build(); // invalid targetPercentage
        }
    }

    // ---------------- GET BY INVESTOR ----------------
    @GetMapping("/investor/{investorId}")
    public ResponseEntity<List<AssetClassAllocationRule>> getByInvestor(@PathVariable Long investorId) {
        List<AssetClassAllocationRule> rules = service.getRulesByInvestor(investorId);
        return ResponseEntity.ok(rules);
    }

    // ---------------- GET ACTIVE RULES ----------------
    @GetMapping("/investor/{investorId}/active")
    public ResponseEntity<List<AssetClassAllocationRule>> getActive(@PathVariable Long investorId) {
        List<AssetClassAllocationRule> activeRules = service.getActiveRulesByInvestor(investorId);
        return ResponseEntity.ok(activeRules);
    }

    // ---------------- GET SINGLE RULE BY ID ----------------
    @GetMapping("/{id}")
    public ResponseEntity<AssetClassAllocationRule> getById(@PathVariable Long id) {
        try {
            AssetClassAllocationRule rule = service.getRuleById(id);
            return ResponseEntity.ok(rule);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // ---------------- GET ALL RULES ----------------
    @GetMapping
    public ResponseEntity<List<AssetClassAllocationRule>> getAll() {
        return ResponseEntity.ok(service.getAllRules());
    }
}
