// package com.example.demo.repository;

// import java.util.List;

// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.stereotype.Repository;
// import org.springframework.data.repository.query.Param;


// import com.example.demo.entity.AssetClassAllocationRule;

// @Repository
// public interface AssetClassAllocationRuleRepository
//         extends JpaRepository<AssetClassAllocationRule, Long> {

//     List<AssetClassAllocationRule> findByInvestorId(Long investorId);

//     @Query("select r from AssetClassAllocationRule r where r.investorId=:investorId and r.active=true")
// List<AssetClassAllocationRule> findActiveRulesHql(@Param("investorId") Long investorId);


// }

package com.example.demo.repository;

import com.example.demo.entity.AssetClassAllocationRule;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssetClassAllocationRuleRepository extends CrudRepository<AssetClassAllocationRule, Long> {
    List<AssetClassAllocationRule> findByInvestorId(Long investorId);
    @Query("SELECT r FROM AssetClassAllocationRule r WHERE r.investorId = :investorId AND r.active = true")
    List<AssetClassAllocationRule> findActiveRulesHql(@Param("investorId") Long investorId);
}
