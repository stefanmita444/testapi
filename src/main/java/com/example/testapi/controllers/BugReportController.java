package com.example.testapi.controllers;

import com.example.testapi.models.*;
import com.example.testapi.services.BugReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<BugReportWrapper> createBugReport(
            @RequestBody BugReportDto bugReport ) {
        log.info("Creating bug report ------------------------");
        BugReportWrapper report = new BugReportWrapper(bugReportService.createBugReport(bugReport));
        log.info("Bug Report Created for: " + report.getData().getId() + " ---------------\n\n");
        return ResponseEntity.status(HttpStatus.OK).body(report);
    }

    @PostMapping("/resolve/{id}/{status}")
    public ResponseEntity<BugReportsList> resolveBugReport(
            @PathVariable String id,
            @PathVariable BugStatus status) {
        log.info("Creating bug report ------------------------");
        List<BugReport> reports = bugReportService.resolveBugReport(id, status);
        log.info("Bug Report Edited for: " + id + " ---------------\n\n");
        return ResponseEntity.status(HttpStatus.OK).body(new BugReportsList(reports));
    }

    @GetMapping
    public ResponseEntity<BugReportsList> getAllBugReports() {
        log.info("Fetching all bug reports ---------------------------");
        List<BugReport> reports = bugReportService.getAllBugReports();
        log.info("Bug Reports Found ---------------------------------");
        return ResponseEntity.status(HttpStatus.OK).body(new BugReportsList(reports));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BugReportWrapper> getBugReportById(
            @PathVariable("id") String id) {
        log.info("Fetching bug report with id: " + id + " ---------------------------");
        BugReport report = bugReportService.getBugReportById(id);
        log.info("Bug Report Found with id: " + id + " ---------------------------------");
        return new ResponseEntity<>(new BugReportWrapper(report), HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<BugReportsList> getBugReportByStatus(
            @PathVariable("status") BugStatus status) {
        log.info("Fetching bug reports with status: " + status + " ---------------------------");
        List<BugReport> reports = bugReportService.getBugReportsByStatus(status);
        log.info("Bug Reports returned ---------------------------------");
        return new ResponseEntity<>(new BugReportsList(reports), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BugReportsList> deleteBugReport(
            @PathVariable String id ) {
        log.info("Initializing Bug Report Deletion------------------------");
        List<BugReport> reports = bugReportService.deleteBugReportById(id);
        log.info("Deletion Complete ----------------------------------");
        return new ResponseEntity<>(new BugReportsList(reports), HttpStatus.OK);
    }

}
