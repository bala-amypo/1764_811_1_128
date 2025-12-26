package com.example.demo.service.impl;

import com.example.demo.entity.HoldingRecord;
import com.example.demo.repository.HoldingRecordRepository;
import com.example.demo.service.HoldingRecordService;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class HoldingRecordServiceImpl implements HoldingRecordService {

    private final HoldingRecordRepository holdingRecordRepository;

    public HoldingRecordServiceImpl(HoldingRecordRepository holdingRecordRepository) {
        this.holdingRecordRepository = holdingRecordRepository;
    }

    @Override
    public HoldingRecord recordHolding(HoldingRecord holding) {
        if (holding.getCurrentValue() <= 0) {
            throw new IllegalArgumentException("Invalid value: must be > 0");
        }
        return holdingRecordRepository.save(holding);
    }

    @Override
    public List<HoldingRecord> getHoldingsByInvestor(Long investorId) {
        return holdingRecordRepository.findByInvestorId(investorId);
    }

    @Override
    public Optional<HoldingRecord> getHoldingById(Long id) {
        return holdingRecordRepository.findById(id);
    }

    @Override
    public List<HoldingRecord> getAllHoldings() {
        return holdingRecordRepository.findAll();
    }
}
