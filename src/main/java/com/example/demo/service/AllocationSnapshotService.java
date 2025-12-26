package com.example.demo.service;

import com.example.demo.entity.AllocationSnapshotRecord;
import com.example.demo.exception.ResourceNotFoundException;
import java.util.List;

public interface AllocationSnapshotService {
    AllocationSnapshotRecord computeSnapshot(Long investorId);
    AllocationSnapshotRecord getSnapshotById(Long id) throws ResourceNotFoundException;
    List<AllocationSnapshotRecord> getSnapshotsByInvestor(Long investorId);
    List<AllocationSnapshotRecord> getAllSnapshots();
}
