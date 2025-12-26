// package com.example.demo.service.impl;

// import com.example.demo.entity.AssetClassAllocationRule;
// import com.example.demo.exception.ResourceNotFoundException;
// import com.example.demo.repository.AssetClassAllocationRuleRepository;
// import com.example.demo.service.AllocationRuleService;
// import org.springframework.stereotype.Service;

// import java.util.List;

// @Service
// public class AllocationRuleServiceImpl implements AllocationRuleService {

//     private final AssetClassAllocationRuleRepository repository;

//     public AllocationRuleServiceImpl(AssetClassAllocationRuleRepository repository) {
//         this.repository = repository;
//     }

//     @Override
//     public AssetClassAllocationRule createRule(AssetClassAllocationRule rule) {
//         if (rule.getTargetPercentage() < 0 || rule.getTargetPercentage() > 100) {
//             throw new IllegalArgumentException("Percentage must be between 0 and 100");
//         }
//         return repository.save(rule);
//     }

//     @Override
//     public AssetClassAllocationRule updateRule(Long id, AssetClassAllocationRule updatedRule) {
//         AssetClassAllocationRule existing = getRuleById(id);
//         existing.setAssetClass(updatedRule.getAssetClass());
//         existing.setTargetPercentage(updatedRule.getTargetPercentage());
//         existing.setActive(updatedRule.getActive());
//         return repository.save(existing);
//     }

//     @Override
//     public List<AssetClassAllocationRule> getRulesByInvestor(Long investorId) {
//         return repository.findByInvestorId(investorId); // ✅ returns List
//     }

//     @Override
//     public List<AssetClassAllocationRule> getActiveRulesByInvestor(Long investorId) {
//         return repository.findActiveRulesHql(investorId); // ✅ returns List
//     }

//     @Override
//     public AssetClassAllocationRule getRuleById(Long id) {
//         return repository.findById(id)
//                 .orElseThrow(() -> new ResourceNotFoundException("Rule not found"));
//     }

//     @Override
//     public List<AssetClassAllocationRule> getAllRules() {
//         return repository.findAll(); // ✅ returns List
//     }
// }




package com.example.demo.service.impl;

import com.example.demo.entity.AssetClassAllocationRule;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.AssetClassAllocationRuleRepository;
import com.example.demo.service.AllocationRuleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllocationRuleServiceImpl
        implements AllocationRuleService {

    private final AssetClassAllocationRuleRepository repository;

    public AllocationRuleServiceImpl(
            AssetClassAllocationRuleRepository repository) {
        this.repository = repository;
    }

    @Override
    public AssetClassAllocationRule createRule(AssetClassAllocationRule rule) {

        if (rule.getTargetPercentage() < 0 || rule.getTargetPercentage() > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
        return repository.save(rule);
    }

    @Override
    public AssetClassAllocationRule updateRule(Long id,
                                               AssetClassAllocationRule updatedRule) {

        AssetClassAllocationRule existing = getRuleById(id);
        existing.setAssetClass(updatedRule.getAssetClass());
        existing.setTargetPercentage(updatedRule.getTargetPercentage());
        existing.setActive(updatedRule.getActive());
        return repository.save(existing);
    }

    @Override
    public List<AssetClassAllocationRule> getRulesByInvestor(Long investorId) {
        return repository.findByInvestorId(investorId);
    }

    @Override
    public List<AssetClassAllocationRule> getActiveRulesByInvestor(Long investorId) {
        return repository.findActiveRulesHql(investorId);
    }

    @Override
    public AssetClassAllocationRule getRuleById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rule not found"));
    }
    @Override
    public List<AssetClassAllocationRule> getAllRules() {
        return repository.findAll();
    }
}


