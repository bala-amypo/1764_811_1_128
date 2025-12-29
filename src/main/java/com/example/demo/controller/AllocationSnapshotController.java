package com.example.demo.controller;

import com.example.demo.entity.AllocationSnapshotRecord;
import com.example.demo.service.impl.AllocationSnapshotServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/snapshots")
public class AllocationSnapshotController {

    private final AllocationSnapshotServiceImpl allocationSnapshotService;

    public AllocationSnapshotController(AllocationSnapshotServiceImpl allocationSnapshotService) {
        this.allocationSnapshotService = allocationSnapshotService;
    }

    @PostMapping("/compute/{investorId}")
    public ResponseEntity<AllocationSnapshotRecord> computeSnapshot(@PathVariable Long investorId) {
        return ResponseEntity.ok(allocationSnapshotService.computeSnapshot(investorId));
    }

    @GetMapping("/investor/{investorId}")
    public ResponseEntity<List<AllocationSnapshotRecord>> getSnapshotsByInvestor(@PathVariable Long investorId) {
        return ResponseEntity.ok(allocationSnapshotService.getSnapshotsByInvestor(investorId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AllocationSnapshotRecord> getSnapshotById(@PathVariable Long id) {
        return ResponseEntity.ok(allocationSnapshotService.getSnapshotById(id));
    }

    @GetMapping
    public ResponseEntity<List<AllocationSnapshotRecord>> getAllSnapshots() {
        return ResponseEntity.ok(allocationSnapshotService.getAllSnapshots());
    }
}
