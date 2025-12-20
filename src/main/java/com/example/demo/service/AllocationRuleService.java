package com.example.demo.service;

import com.example.demo.entity.AssetClassAllocationRule;
import java.util.List;

public interface AllocationRuleService {

    AssetClassAllocationRule createRule(AssetClassAllocationRule rule);

    AssetClassAllocationRule updateRule(Long id, AssetClassAllocationRule updatedRule);

    List<AssetClassAllocationRule> getRulesByInvestor(Long investorId);

    List<AssetClassAllocationRule> getActiveRulesByInvestor(Long investorId);

    AssetClassAllocationRule getRuleById(Long id);

    List<AssetClassAllocationRule> getAllRules(); // NEW
}
