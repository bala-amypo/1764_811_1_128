package com.example.demo.repository;

import com.example.demo.entity.AssetClassAllocationRule;
// import com.example.demo.entity.enums.AssetClassType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetClassAllocationRuleRepository extends JpaRepository<AssetClassAllocationRule, Long> {
    List<AssetClassAllocationRule> findByInvestorId(Long investorId);

    @Query("SELECT r FROM AssetClassAllocationRule r WHERE r.investorId = :investorId AND r.active = true")
    List<AssetClassAllocationRule> findActiveRulesHql(Long investorId);

    List<AssetClassAllocationRule> findByInvestorIdAndActiveTrue(Long investorId);
}
