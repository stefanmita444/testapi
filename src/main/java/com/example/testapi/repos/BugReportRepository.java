package com.example.testapi.repos;

import com.example.testapi.models.BugReport;
import com.example.testapi.models.BugStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BugReportRepository extends MongoRepository<BugReport, String> {
    List<BugReport> findBugReportsByStatus(BugStatus status);
}
