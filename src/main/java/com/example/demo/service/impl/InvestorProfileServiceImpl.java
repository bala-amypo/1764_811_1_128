package com.example.demo.service.impl;

import com.example.demo.entity.InvestorProfile;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.InvestorProfileRepository;
import com.example.demo.service.InvestorProfileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InvestorProfileServiceImpl implements InvestorProfileService {

    private final InvestorProfileRepository repo;

    public InvestorProfileServiceImpl(InvestorProfileRepository repo) {
        this.repo = repo;
    }

    @Transactional
    @Override
    public InvestorProfile createInvestor(InvestorProfile investor) {
        return repo.save(investor);
    }

    @Override
    public InvestorProfile getInvestorById(Long id) {
        return repo.findById(id)
                   .orElseThrow(() ->
                       new ResourceNotFoundException("Investor not found with id " + id));
    }

    @Override
    public InvestorProfile findByInvestorId(String investorId) {
        return repo.findByInvestorId(investorId)
                   .orElseThrow(() ->
                       new ResourceNotFoundException("Investor not found with investorId " + investorId));
    }

    @Override
    public List<InvestorProfile> getAllInvestors() {
        return repo.findAll();
    }

    @Transactional
    @Override
    public InvestorProfile updateInvestorStatus(Long id, boolean active) {
        InvestorProfile investor = getInvestorById(id);
        investor.setActive(active);
        return repo.save(investor);
    }

    @Transactional
    public void deleteInvestor(Long id) {
        InvestorProfile investor = getInvestorById(id);
        repo.delete(investor);
    }
}
