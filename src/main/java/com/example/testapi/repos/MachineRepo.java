package com.example.testapi.repos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.testapi.models.Machines;

public interface MachineRepo extends CrudRepository<Machines, Long> {
    List<Machines> findAll();
}
