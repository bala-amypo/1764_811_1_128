package com.example.demo.service;

import com.example.demo.entity.RebalancingAlertRecord;
import com.example.demo.exception.ResourceNotFoundException;
import java.util.List;

public interface RebalancingAlertService {
    RebalancingAlertRecord createAlert(RebalancingAlertRecord alert);
    RebalancingAlertRecord resolveAlert(Long id) throws ResourceNotFoundException;
    List<RebalancingAlertRecord> getAlertsByInvestor(Long investorId);
    RebalancingAlertRecord getAlertById(Long id) throws ResourceNotFoundException;
    List<RebalancingAlertRecord> getAllAlerts();
}
