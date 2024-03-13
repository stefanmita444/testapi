package com.example.testapi.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.testapi.models.BugReport;
import com.example.testapi.models.BugReportDto;
import com.example.testapi.models.BugStatus;
import com.example.testapi.models.ResponseWrapper;
import com.example.testapi.services.BugReportService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
//@Api(value = "Bug Report Controller", description = "APIs related to Reporting bugs operations")
@RequiredArgsConstructor
@RequestMapping("/api/v1/bug-reports")
@Tag(name = "Bug Reports")
public class BugReportController {

    private final BugReportService bugReportService;

    @PostMapping
    public ResponseEntity<ResponseWrapper<BugReport>> createBugReport(
            @RequestBody BugReportDto bugReport ) {
        log.info("Creating bug report ------------------------");
        BugReport report = bugReportService.createBugReport(bugReport);
        log.info("Bug Report Created for: " + report.getId() + " ---------------\n\n");
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper<>(report));
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<BugReport>>> getAllBugReports() {
        log.info("Fetching all bug reports ---------------------------");
        List<BugReport> reports = bugReportService.getAllBugReports();
        log.info("Bug Reports Found ---------------------------------");
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper<>(reports));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<BugReport>> getBugReportById(
            @PathVariable("id") String id) {
        log.info("Fetching bug report with id: " + id + " ---------------------------");
        BugReport report = bugReportService.getBugReportById(id);
        log.info("Bug Report Found with id: " + id + " ---------------------------------");
        return new ResponseEntity<>(new ResponseWrapper<>(report), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<List<BugReport>>> deleteBugReport(
            @PathVariable String id ) {
        log.info("Initializing Bug Report Deletion------------------------");
        List<BugReport> reports = bugReportService.deleteBugReportById(id);
        log.info("Deletion Complete ----------------------------------");
        return new ResponseEntity<>(new ResponseWrapper<>(reports), HttpStatus.OK);
    }



}
