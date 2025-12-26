package com.example.demo.repository;

import com.example.demo.entity.HoldingRecord;
import com.example.demo.entity.enums.AssetClassType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HoldingRecordRepository extends JpaRepository<HoldingRecord, Long> {

    List<HoldingRecord> findByInvestorId(Long investorId);

    // Satisfies Test Priority 66 & Runtime Query Creation
    @Query("SELECT h FROM HoldingRecord h WHERE h.investorId = :investorId AND h.assetClass = :assetClass")
    List<HoldingRecord> findByInvestorAndAssetClass(@Param("investorId") Long investorId, 
                                                    @Param("assetClass") AssetClassType assetClass);

    // Satisfies Test Priority 64, 65 & Runtime Query Creation
    @Query("SELECT h FROM HoldingRecord h WHERE h.currentValue > :val")
    List<HoldingRecord> findByValueGreaterThan(@Param("val") Double val);

    // Standard naming convention for internal service use
    List<HoldingRecord> findByInvestorIdAndAssetClass(Long investorId, AssetClassType assetClass);
}