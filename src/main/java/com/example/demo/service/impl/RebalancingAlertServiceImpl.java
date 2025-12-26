package com.example.demo.service.impl;

import com.example.demo.entity.RebalancingAlertRecord;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.RebalancingAlertRecordRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class RebalancingAlertServiceImpl {

    private final RebalancingAlertRecordRepository repo;

    public RebalancingAlertServiceImpl(RebalancingAlertRecordRepository repo) {
        this.repo = repo;
    }

    public RebalancingAlertRecord createAlert(RebalancingAlertRecord alert) {
        if (alert.getCurrentPercentage() <= alert.getTargetPercentage())
            throw new IllegalArgumentException("currentPercentage > targetPercentage");

        return repo.save(alert);
    }

    public RebalancingAlertRecord resolveAlert(Long id) {
        RebalancingAlertRecord alert = repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Alert not found with id " + id));
        alert.setResolved(true);
        return repo.save(alert);
    }

    public List<RebalancingAlertRecord> getAlertsByInvestor(Long investorId) {
        return repo.findByInvestorId(investorId);
    }
}
