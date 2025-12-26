package com.example.demo.service;

import com.example.demo.entity.InvestorProfile;
import com.example.demo.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;

public interface InvestorProfileService {
    InvestorProfile createInvestor(InvestorProfile investor);
    InvestorProfile getInvestorById(Long id) throws ResourceNotFoundException;
    Optional<InvestorProfile> findByInvestorId(String investorId);
    List<InvestorProfile> getAllInvestors();
    InvestorProfile updateInvestorStatus(Long id, boolean active) throws ResourceNotFoundException;
}
