package com.example.testapi.models;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bugReport")
public class BugReport {

    @Id
    private String id;
    @NotNull(message = "Please provide the Description")
    private String description;
    private BugStatus status = BugStatus.OPEN;
    private LocalDateTime timestamp;

}

