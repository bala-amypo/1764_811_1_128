package com.example.demo.service;

import com.example.demo.entity.AssetClassAllocationRule;
import com.example.demo.exception.ResourceNotFoundException;

import java.util.List;

public interface AllocationRuleService {

    AssetClassAllocationRule createRule(AssetClassAllocationRule rule);

    AssetClassAllocationRule updateRule(Long id, AssetClassAllocationRule updatedRule) throws ResourceNotFoundException;

    List<AssetClassAllocationRule> getRulesByInvestor(Long investorId);

    List<AssetClassAllocationRule> getActiveRules(Long investorId);

    AssetClassAllocationRule getRuleById(Long id) throws ResourceNotFoundException;

    List<AssetClassAllocationRule> getAllRules(); // <-- add this
}
