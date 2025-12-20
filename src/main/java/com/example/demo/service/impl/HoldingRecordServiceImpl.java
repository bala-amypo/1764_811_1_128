package com.example.demo.service.impl;

import com.example.demo.entity.HoldingRecord;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.HoldingRecordRepository;
import com.example.demo.service.HoldingRecordService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoldingRecordServiceImpl implements HoldingRecordService {

    private final HoldingRecordRepository repo;

    public HoldingRecordServiceImpl(HoldingRecordRepository repo) {
        this.repo = repo;
    }

    @Override
    public HoldingRecord recordHolding(HoldingRecord holding) {
        holding.setCurrentValue(holding.getCurrentValue()); // validate
        return repo.save(holding);
    }

    @Override
    public List<HoldingRecord> getHoldingsByInvestor(Long investorId) {
        return repo.findByInvestorId(investorId);
    }

    @Override
    public HoldingRecord getHoldingById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Holding not found"));
    }

    @Override
    public List<HoldingRecord> getAllHoldings() {
        return repo.findAll();
    }
}
