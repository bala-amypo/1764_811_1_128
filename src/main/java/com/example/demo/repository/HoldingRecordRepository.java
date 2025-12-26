package com.example.demo.repository;

import com.example.demo.entity.HoldingRecord;
import com.example.demo.entity.enums.AssetClassType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HoldingRecordRepository extends JpaRepository<HoldingRecord, Long> {

    // Find all holdings for a specific investor
    List<HoldingRecord> findByInvestorId(Long investorId);

    // Find all holdings above a certain value
    List<HoldingRecord> findByCurrentValueGreaterThan(Double currentValue);

    // This matches your test: find by investor and asset class
    List<HoldingRecord> findByInvestorIdAndAssetClass(Long investorId, AssetClassType assetClass);
}
