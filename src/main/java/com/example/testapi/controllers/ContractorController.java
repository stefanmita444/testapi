package com.example.testapi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.testapi.models.Contractor;
import com.example.testapi.services.ContractorService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/data")
@PreAuthorize("isAuthenticated()")
public class ContractorController {

    @Autowired
    ContractorService contractorService;

    @GetMapping
    public ResponseEntity<List<Contractor>> getContractor() {
        List<Contractor> contractors = contractorService.getContractors();
        return ResponseEntity.ok(contractors);
    }

    @PostMapping
    public ResponseEntity<Contractor> createContractor(@RequestBody Contractor contractor) {
        Contractor newContractor = contractorService.createContractor(contractor);
        return ResponseEntity.ok(newContractor);
    }

}
