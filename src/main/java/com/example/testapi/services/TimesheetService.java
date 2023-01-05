package com.example.testapi.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.testapi.models.Timesheet;
import com.example.testapi.repos.TimesheetRepo;

@Service
public class TimesheetService {

    @Autowired
    TimesheetRepo timesheetRepo;

    public List<Timesheet> getTimesheets() {
        return timesheetRepo.findAll();
    }

    public Timesheet createTimesheet(Timesheet timesheet) {
        return timesheetRepo.save(timesheet);
    }

}
