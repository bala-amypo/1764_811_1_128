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
        // enforce uniqueness: business/investorId should be unique
        repository.findByInvestorId(investor.getInvestorId())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Investor ID already exists");
                });

        // set default flags if needed
        if (investor.getActive() == null) {
            investor.setActive(true);
        }
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
