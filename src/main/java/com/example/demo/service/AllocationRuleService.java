package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.AssetClassAllocationRule;

public interface AllocationRuleService {
    

    AssetClassAllocationRule createRule(AssetClassAllocationRule rule);

    AssetClassAllocationRule updateRule(Long id, AssetClassAllocationRule updatedRule);

    List<AssetClassAllocationRule> getRulesByInvestor(Long investorId);

    List<AssetClassAllocationRule> getActiveRulesByInvestor(Long investorId);

    AssetClassAllocationRule getRuleById(Long id);
}
