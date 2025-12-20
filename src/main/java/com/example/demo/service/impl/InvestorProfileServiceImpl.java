package com.example.demo.service.impl;

import com.example.demo.entity.InvestorProfile;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.InvestorProfileRepository;
import com.example.demo.service.InvestorProfileService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvestorProfileServiceImpl implements InvestorProfileService {

    private final InvestorProfileRepository repository;

    public InvestorProfileServiceImpl(InvestorProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public InvestorProfile createInvestor(InvestorProfile investor) {
        return repository.save(investor);
    }

    @Override
    public InvestorProfile getInvestorById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Investor not found"));
    }

    @Override
    public InvestorProfile findByInvestorId(String investorId) {
        return repository.findByInvestorId(investorId)
                .orElseThrow(() -> new ResourceNotFoundException("Investor not found"));
    }

    @Override
    public List<InvestorProfile> getAllInvestors() {
        return repository.findAll();
    }

    @Override
    public InvestorProfile updateInvestorStatus(Long id, boolean active) {
        InvestorProfile investor = getInvestorById(id);
        investor.setActive(active);
        return repository.save(investor);
    }
}
