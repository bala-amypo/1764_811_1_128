package com.example.demo.repository;

import com.example.demo.entity.HoldingRecord;
import com.example.demo.entity.enums.AssetClassType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoldingRecordRepository
        extends JpaRepository<HoldingRecord, Long> {

    List<HoldingRecord> findByInvestorId(Long investorId);

    // FIXED
    List<HoldingRecord> findByCurrentValueGreaterThan(Double currentValue);

    // FIXED
    List<HoldingRecord> findByInvestorIdAndAssetClass(
            Long investorId,
            AssetClassType assetClass
    );
}

