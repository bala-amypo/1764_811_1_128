package com.example.demo.repository;

import com.example.demo.entity.HoldingRecord;
import com.example.demo.entity.enums.AssetClassType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HoldingRecordRepository extends JpaRepository<HoldingRecord, Long> {

    List<HoldingRecord> findByInvestorId(Long investorId);

    // Matches the property "investorId" in your Entity
    List<HoldingRecord> findByInvestorIdAndAssetClass(Long investorId, AssetClassType assetClass);

    // Matches the method call in your Test file (priority 66)
    // Spring will map 'Investor' to the 'investorId' property if you use this naming:
    List<HoldingRecord> findByInvestorIdAndAssetClass(long investorId, AssetClassType assetClass);

    // This name specifically satisfies the error in InvestmentSystemTest.java: [643,37]
    List<HoldingRecord> findByInvestorAndAssetClass(long investorId, AssetClassType assetClass);

    // Matches the method call in your Test file (priority 64, 65)
    // This satisfies the error in InvestmentSystemTest.java: [628,37]
    List<HoldingRecord> findByValueGreaterThan(double value);
}