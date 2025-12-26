package com.example.demo.service.impl;

import com.example.demo.entity.HoldingRecord;
import com.example.demo.entity.enums.AssetClassType;
import com.example.demo.repository.HoldingRecordRepository;
import com.example.demo.service.HoldingRecordService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class HoldingRecordServiceImpl implements HoldingRecordService {

    private final HoldingRecordRepository holdingRecordRepository;

    public HoldingRecordServiceImpl(HoldingRecordRepository holdingRecordRepository) {
        this.holdingRecordRepository = holdingRecordRepository;
    }

    @Override
    public HoldingRecord recordHolding(HoldingRecord holding) {
        holding.validateValue();
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

    @Override
public List<HoldingRecord> getHoldingsByInvestorAndAssetClass(Long investorId, AssetClassType assetClass) {
    // Calling the method that now has the @Query annotation
    return holdingRecordRepository.findByInvestorAndAssetClass(investorId, assetClass);
}
}