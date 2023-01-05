package com.example.testapi.repos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.testapi.models.Timesheet;

public interface TimesheetRepo extends CrudRepository<Timesheet, Long> {
    List<Timesheet> findAll();
}
