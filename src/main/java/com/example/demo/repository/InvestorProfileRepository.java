package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.InvestorProfile;

@Repository
public interface InvestorProfileRepository extends JpaRepository<InvestorProfile,Long>{

    Optional<InvestorProfile> findByInvestorId(String investorId);
    
}
