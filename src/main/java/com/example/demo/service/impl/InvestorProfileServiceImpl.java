package com.example.demo.service.impl;

import com.example.demo.entity.InvestorProfile;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.InvestorProfileRepository;
import com.example.demo.service.InvestorProfileService;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class InvestorProfileServiceImpl implements InvestorProfileService {

    private final InvestorProfileRepository investorProfileRepository;

    public InvestorProfileServiceImpl(InvestorProfileRepository investorProfileRepository) {
        this.investorProfileRepository = investorProfileRepository;
    }

    @Override
    public InvestorProfile createInvestor(InvestorProfile investor) {
        if (investor.getActive() == null) {
            investor.setActive(true);
        }
        return investorProfileRepository.save(investor);
    }

    @Override
    public InvestorProfile getInvestorById(Long id) {
        return investorProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Investor not found: " + id));
    }

    @Override
    public Optional<InvestorProfile> findByInvestorId(String investorId) {
        return investorProfileRepository.findByInvestorId(investorId);
    }

    @Override
    public List<InvestorProfile> getAllInvestors() {
        return investorProfileRepository.findAll();
    }

    @Override
    public InvestorProfile updateInvestorStatus(Long id, boolean active) {
        InvestorProfile investor = investorProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Investor not found: " + id));
        investor.setActive(active);
        return investorProfileRepository.save(investor);
    }
}
