package com.example.demo.service;

import com.example.demo.entity.HoldingRecord;
import com.example.demo.entity.enums.AssetClassType; // <--- ADD THIS IMPORT
import java.util.List;
import java.util.Optional;

public interface HoldingRecordService {
    HoldingRecord recordHolding(HoldingRecord holding);
    List<HoldingRecord> getHoldingsByInvestor(Long investorId);
    Optional<HoldingRecord> getHoldingById(Long id);
    List<HoldingRecord> getAllHoldings();
    // This line was causing the error because AssetClassType wasn't imported
    List<HoldingRecord> getHoldingsByInvestorAndAssetClass(Long investorId, AssetClassType assetClass);
}