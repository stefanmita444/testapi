package com.example.testapi.repos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.testapi.models.Contractor;

public interface ContractorRepo extends CrudRepository<Contractor, Long> {
    List<Contractor> findAll();
}
