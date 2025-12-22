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

    @PostMapping
    public ResponseEntity<InvestorProfile> createInvestor(
             @RequestBody InvestorProfile investor) {

        InvestorProfile created = service.createInvestor(investor);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvestorProfile> getById(@PathVariable Long id) {
        InvestorProfile investor = service.getInvestorById(id);
        return ResponseEntity.ok(investor);
    }

    @GetMapping("/lookup/{investorId}")
    public ResponseEntity<InvestorProfile> getByBusinessInvestorId(
            @PathVariable String investorId) {

        InvestorProfile investor = service.findByInvestorId(investorId);
        return ResponseEntity.ok(investor);
    }

    @GetMapping
    public ResponseEntity<List<InvestorProfile>> getAll() {
        List<InvestorProfile> list = service.getAllInvestors();
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}/status/{active}")
    public ResponseEntity<InvestorProfile> updateStatus(
            @PathVariable Long id,
            @PathVariable boolean active) {

        InvestorProfile updated =
                service.updateInvestorStatus(id, active);

        return ResponseEntity.ok(updated);
    }
}
