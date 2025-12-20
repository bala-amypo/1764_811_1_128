package com.example.demo.controller;

import com.example.demo.entity.InvestorProfile;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.InvestorProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/investors")
public class InvestorProfileController {

    private final InvestorProfileService service;

    public InvestorProfileController(InvestorProfileService service) {
        this.service = service;
    }

    // ---------------- CREATE ----------------
    @PostMapping
    public ResponseEntity<InvestorProfile> createInvestor(@RequestBody InvestorProfile investor) {
        try {
            InvestorProfile created = service.createInvestor(investor);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // uniqueness violation
        }
    }

    // ---------------- GET BY INTERNAL ID ----------------
    @GetMapping("/{id}")
    public ResponseEntity<InvestorProfile> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.getInvestorById(id));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // ---------------- GET BY BUSINESS INVESTOR ID ----------------
    @GetMapping("/lookup/{investorId}")
    public ResponseEntity<InvestorProfile> getByBusinessInvestorId(@PathVariable String investorId) {
        try {
            return ResponseEntity.ok(service.findByInvestorId(investorId));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // ---------------- GET ALL ----------------
    @GetMapping
    public ResponseEntity<List<InvestorProfile>> getAll() {
        List<InvestorProfile> list = service.getAllInvestors();
        return ResponseEntity.ok(list);
    }

    // ---------------- UPDATE ACTIVE STATUS ----------------
    @PutMapping("/{id}/status/{active}")
    public ResponseEntity<InvestorProfile> updateStatus(@PathVariable Long id,
                                                        @PathVariable boolean active) {
        try {
            InvestorProfile updated = service.updateInvestorStatus(id, active);
            return ResponseEntity.ok(updated);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
