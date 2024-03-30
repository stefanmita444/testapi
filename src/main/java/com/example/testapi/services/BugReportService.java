package com.example.testapi.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.testapi.exceptions.CustomException;
import com.example.testapi.models.BugReport;
import com.example.testapi.models.BugReportDto;
import com.example.testapi.repos.BugReportRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
                        .build());
    }

    public List<BugReport> getAllBugReports() {
        return bugReportRepository.findAll();
    }

    public BugReport getBugReportById(String id) {
        BugReport report = bugReportRepository.findById(id).orElseThrow(() -> new CustomException("No report found with id: " + id));
        return report;
    }


    public List<BugReport> deleteBugReportById(String id) {
        log.info("Fetching user with id: " + id);

        BugReport report = bugReportRepository.findById(id).orElseThrow(() -> new CustomException("No report found with id: " + id));

        log.info("Deleting bug report: " + id);

        bugReportRepository.delete(report);

        log.info("Bug report deleted");

        return getAllBugReports();
    }
}

