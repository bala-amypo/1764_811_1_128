package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.RebalancingAlertRecord;

@Repository
public interface RebalancingAlertRecordRepository extends JpaRepository<RebalancingAlertRecord,Long>{

    List<RebalancingAlertRecord> findByInvestorId(Long investorId);

    List<RebalancingAlertRecord> findByResolvedFalse();
    
}
