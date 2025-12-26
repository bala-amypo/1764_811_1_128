package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.AssetClassAllocationRule;

@Repository
public interface AssetClassAllocationRuleRepository
        extends JpaRepository<AssetClassAllocationRule, Long> {

    List<AssetClassAllocationRule> findByInvestorId(Long investorId);

    @Query("select r from AssetClassAllocationRule r where r.investorId=:investorId and r.active=true")
    List<AssetClassAllocationRule> findActiveRulesHql(Long investorId);
}

