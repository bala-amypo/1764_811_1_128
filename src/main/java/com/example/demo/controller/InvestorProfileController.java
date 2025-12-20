package com.example.demo.controller;

import com.example.demo.entity.InvestorProfile;
import com.example.demo.service.InvestorProfileService;
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
    public InvestorProfile create(@RequestBody InvestorProfile investor) {
        return service.createInvestor(investor);
    }

    // ---------------- GET BY ID ----------------
    @GetMapping("/{id}")
    public InvestorProfile getById(@PathVariable Long id) {
        return service.getInvestorById(id);
    }

    // ---------------- GET ALL ----------------
    @GetMapping
    public List<InvestorProfile> getAll() {
        return service.getAllInvestors();
    }

    // ---------------- UPDATE STATUS ----------------
    @PutMapping("/{id}/status/{active}")
    public InvestorProfile updateStatus(@PathVariable Long id,
                                        @PathVariable boolean active) {
        return service.updateInvestorStatus(id, active);
    }

    // ---------------- LOOKUP BY BUSINESS INVESTOR ID ----------------
    @GetMapping("/lookup/{investorId}")
    public InvestorProfile getByBusinessInvestorId(@PathVariable Long investorId) {
        return service.getInvestorByBusinessId(investorId);
    }
}
