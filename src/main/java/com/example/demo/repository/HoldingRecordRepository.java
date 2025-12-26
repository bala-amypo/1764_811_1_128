package com.example.demo.repository;

import com.example.demo.entity.HoldingRecord;
import com.example.demo.entity.enums.AssetClassType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HoldingRecordRepository extends JpaRepository<HoldingRecord, Long> {
    List<HoldingRecord> findByInvestorId(Long investorId);

    List<HoldingRecord> findByValueGreaterThan(Double value);

    // If your HoldingRecord has a field called investorId
List<HoldingRecord> findByInvestorIdAndAssetClass(Long investorId, AssetClassType assetClass);

}
