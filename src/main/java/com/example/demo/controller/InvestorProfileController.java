package com.example.demo.controller;

import com.example.demo.entity.InvestorProfile;
// import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.impl.InvestorProfileServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/investors")
public class InvestorProfileController {

    private final InvestorProfileServiceImpl investorProfileService;

    public InvestorProfileController(InvestorProfileServiceImpl investorProfileService) {
        this.investorProfileService = investorProfileService;
    }

    @PostMapping
    public ResponseEntity<InvestorProfile> createInvestor(@RequestBody InvestorProfile investor) {
        return ResponseEntity.ok(investorProfileService.createInvestor(investor));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvestorProfile> getInvestorById(@PathVariable Long id) {
        return ResponseEntity.ok(investorProfileService.getInvestorById(id));
    }

    @GetMapping
    public ResponseEntity<List<InvestorProfile>> getAllInvestors() {
        return ResponseEntity.ok(investorProfileService.getAllInvestors());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<InvestorProfile> updateInvestorStatus(@PathVariable Long id,
                                                                @RequestParam boolean active) {
        return ResponseEntity.ok(investorProfileService.updateInvestorStatus(id, active));
    }

    @GetMapping("/lookup/{investorId}")
    public ResponseEntity<Optional<InvestorProfile>> findByInvestorId(@PathVariable String investorId) {
        return ResponseEntity.ok(investorProfileService.findByInvestorId(investorId));
    }
}
