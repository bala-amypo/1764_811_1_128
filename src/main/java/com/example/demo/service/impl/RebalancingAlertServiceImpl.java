package com.example.demo.service.impl;

import com.example.demo.entity.RebalancingAlertRecord;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.RebalancingAlertRecordRepository;
import com.example.demo.service.RebalancingAlertService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RebalancingAlertServiceImpl implements RebalancingAlertService {

    private final RebalancingAlertRecordRepository alertRepository;

    public RebalancingAlertServiceImpl(
            RebalancingAlertRecordRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Override
    public RebalancingAlertRecord createAlert(RebalancingAlertRecord alert) {

        if (alert.getCurrentPercentage() <= alert.getTargetPercentage()) {
            throw new IllegalArgumentException(
                    "currentPercentage > targetPercentage"
            );
        }

        return alertRepository.save(alert);
    }

    @Override
    public RebalancingAlertRecord resolveAlert(Long id) {

        RebalancingAlertRecord alert = alertRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Alert not found"));

        alert.setResolved(true);
        return alertRepository.save(alert);
    }

    @Override
    public List<RebalancingAlertRecord> getAlertsByInvestor(Long investorId) {
        return alertRepository.findByInvestorId(investorId);
    }

    @Override
    public RebalancingAlertRecord getAlertById(Long id) {
        return alertRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Alert not found"));
    }

    @Override
    public List<RebalancingAlertRecord> getAllAlerts() {
        return alertRepository.findAll();
    }
}





// package com.example.demo.service.impl;

// import com.example.demo.entity.RebalancingAlertRecord;
// import com.example.demo.exception.ResourceNotFoundException;
// import com.example.demo.repository.RebalancingAlertRecordRepository;
// import com.example.demo.service.RebalancingAlertService;
// import org.springframework.stereotype.Service;

// import java.util.List;

// @Service
// public class RebalancingAlertServiceImpl implements RebalancingAlertService {

//     private final RebalancingAlertRecordRepository repo;

//     public RebalancingAlertServiceImpl(RebalancingAlertRecordRepository repo) {
//         this.repo = repo;
//     }

//     @Override
//     public RebalancingAlertRecord createAlert(RebalancingAlertRecord alert) {
//         alert.validatePercentages();
//         return repo.save(alert);
//     }

//     @Override
//     public RebalancingAlertRecord resolveAlert(Long id) {
//         RebalancingAlertRecord alert = getAlertById(id);
//         alert.setResolved(true);
//         return repo.save(alert);
//     }

//     @Override
//     public List<RebalancingAlertRecord> getAlertsByInvestor(Long investorId) {
//         return repo.findByInvestorId(investorId);
//     }

//     @Override
//     public RebalancingAlertRecord getAlertById(Long id) {
//         return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Alert not found"));
//     }

//     @Override
//     public List<RebalancingAlertRecord> getAllAlerts() {
//         return repo.findAll();
//     }
// }
