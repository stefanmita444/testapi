package com.example.testapi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.testapi.models.Machines;
import com.example.testapi.services.MachineService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/machines")
public class MachineController {

    @Autowired
    MachineService machineService;

    @GetMapping
    public ResponseEntity<List<Machines>> getContractor() {
        List<Machines> machines = machineService.getMachines();
        return ResponseEntity.ok(machines);
    }

    @PostMapping
    public ResponseEntity<Machines> createContractor(@RequestBody Machines machine) {
        Machines newMachine = machineService.creatMachine(machine);
        return ResponseEntity.ok(newMachine);
    }

}
