package com.example.testapi.repos;

import com.example.testapi.models.BugReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BugReportRepository extends MongoRepository<BugReport, String> {
}
