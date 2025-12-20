package com.example.demo.controller;

import com.example.demo.entity.AllocationSnapshotRecord;
import com.example.demo.service.AllocationSnapshotService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/snapshots")
public class AllocationSnapshotController {

    private final AllocationSnapshotService service;

    public AllocationSnapshotController(AllocationSnapshotService service) {
        this.service = service;
    }

    @PostMapping("/compute/{investorId}")
    public AllocationSnapshotRecord computeSnapshot(
            @PathVariable Long investorId) {
        return service.computeSnapshot(investorId);
    }

    @GetMapping("/{id}")
    public AllocationSnapshotRecord getById(@PathVariable Long id) {
        return service.getSnapshotById(id);
    }

    @GetMapping("/investor/{investorId}")
    public List<AllocationSnapshotRecord> getByInvestor(
            @PathVariable Long investorId) {
        return service.getSnapshotsByInvestor(investorId);
    }

    @GetMapping
    public List<AllocationSnapshotRecord> getAll() {
        return service.getAllSnapshots();
    }
}
