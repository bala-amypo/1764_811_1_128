package com.example.demo.repository;

import com.example.demo.entity.HoldingRecord;
import com.example.demo.entity.enums.AssetClassType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HoldingRecordRepository extends JpaRepository<HoldingRecord, Long> {

    List<HoldingRecord> findByInvestorId(Long investorId);

    // Keep this for the Service
    List<HoldingRecord> findByInvestorIdAndAssetClass(Long investorId, AssetClassType assetClass);
    
    // Add this specifically for the Test file (InvestmentSystemTest)
    List<HoldingRecord> findByInvestorAndAssetClass(Long investorId, AssetClassType assetClass);

    // Add this specifically for the Test file (InvestmentSystemTest)
    List<HoldingRecord> findByValueGreaterThan(Double value);

    // Keep your original one if needed
    List<HoldingRecord> findByCurrentValueGreaterThan(Double currentValue);
}