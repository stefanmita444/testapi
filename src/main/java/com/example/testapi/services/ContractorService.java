package com.example.testapi.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.testapi.models.Contractor;
import com.example.testapi.repos.ContractorRepo;

@Service
public class ContractorService {

    @Autowired
    ContractorRepo contractorRepo;

    public List<Contractor> getContractors() {
        return contractorRepo.findAll();
    }

    public Contractor getContractorById(Long id) {
        Optional<Contractor> contractor = contractorRepo.findById(id);
        if (contractor.isPresent()) {
            return contractor.get();
        }

        return null;
    }

    public Contractor createContractor(Contractor contractor) {
        return contractorRepo.save(contractor);
    }

}
