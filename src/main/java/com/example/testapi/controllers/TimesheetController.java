package com.example.testapi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.testapi.models.Timesheet;
import com.example.testapi.services.TimesheetService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/timesheets")
public class TimesheetController {

    @Autowired
    TimesheetService timesheetService;

    @GetMapping
    public ResponseEntity<List<Timesheet>> getAll() {
        List<Timesheet> timesheets = timesheetService.getTimesheets();
        return ResponseEntity.ok(timesheets);
    }

    @PostMapping
    public ResponseEntity<Timesheet> create(@RequestBody Timesheet timesheet) {
        Timesheet newTimesheet = timesheetService.createTimesheet(timesheet);
        return ResponseEntity.ok(newTimesheet);
    }

}
