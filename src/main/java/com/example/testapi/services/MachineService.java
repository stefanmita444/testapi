package com.example.testapi.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.testapi.models.Machines;
import com.example.testapi.repos.MachineRepo;

@Service
public class MachineService {

    @Autowired
    MachineRepo machineRepo;

    public List<Machines> getMachines() {
        return machineRepo.findAll();
    }

    public Machines creatMachine(Machines machine) {
        return machineRepo.save(machine);
    }

}
