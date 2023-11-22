package com.example.testapi.services;

import com.example.testapi.exceptions.ResourceNotFoundException;
import com.example.testapi.models.BugReport;
import com.example.testapi.models.BugReportDto;
import com.example.testapi.models.BugStatus;
import com.example.testapi.repos.BugReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BugReportService {

    private final BugReportRepository bugReportRepository;

    public BugReport createBugReport(BugReportDto bugReport) {
        return bugReportRepository
                .save(BugReport
                        .builder()
                        .description(bugReport.getDescription())
                        .timestamp(LocalDateTime.now(ZoneId.of("America/Chicago")))
                        .status(BugStatus.OPEN)
                        .build());
    }

    public List<BugReport> getAllBugReports() {
        return bugReportRepository.findAll();
    }

    public List<BugReport> resolveBugReport(String id, BugStatus status) {
        log.info("Fetching bug report with id: " + id);
        BugReport report = getBugReportById(id);
        log.info("Bug Report Found");
        report.setStatus(status);
        log.info("Bug report status set as: " + status);
        log.info("Bug Report saving in DB");
        bugReportRepository.save(report);
        return getBugReportsByStatus(BugStatus.OPEN);
    }

    public BugReport getBugReportById(String id) {
        return bugReportRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No bug report found with that id\n\n"));
    }

    public List<BugReport> getBugReportsByStatus(BugStatus status) {
        return bugReportRepository
                .findBugReportsByStatus(status);
    }

    public List<BugReport> deleteBugReportById(String id) {
        log.info("Fetching user with id: " + id);

        BugReport report = getBugReportById(id);

        log.info("Deleting bug report: " + id);

        bugReportRepository.delete(report);

        log.info("Bug report deleted");

        return getBugReportsByStatus(BugStatus.OPEN);
    }
}

