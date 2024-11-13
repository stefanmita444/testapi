package com.example.testapi.services;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.CollectionUtils;

import com.example.testapi.models.BugReport;
import com.example.testapi.models.BugReportDto;
import com.example.testapi.repos.BugReportRepository;

@ContextConfiguration(classes = { BugReportService.class })
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class BugReportServiceTest {

    @Autowired
    BugReportService service;

    @MockBean
    private BugReportRepository repository;

    @Test
    public void bugReportService_createBugReport_ReturnsBugReportDto() {

        BugReport bugReport = BugReport.builder()
                .description("report")
                .build();

        BugReportDto bugReportDto = BugReportDto.builder()
                .description("report")
                .build();

        when(repository.save(Mockito.any(BugReport.class))).thenReturn(bugReport);

        BugReport savedBugReport = service.createBugReport(bugReportDto);

        assertNotNull(savedBugReport);

    }

    @Test
    public void getAllBugReports() {
        BugReport entity = new BugReport();
        entity.setDescription("someDescription");
        BugReportDto dto = BugReportDto.builder().description(entity.getDescription()).build();

        List<BugReport> expected = List.of(entity);

        when(repository.findAll()).thenReturn(expected);

        List<BugReport> actualList = service.getAllBugReports();

        BugReport actual = CollectionUtils.firstElement(actualList);

        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(expected.size(), actualList.size()),
                () -> assertEquals(dto.getDescription(), actual.getDescription()),
                () -> verify(repository).findAll());
    }

    @Test
    public void getBugReportById() {
        BugReport entity = new BugReport();
        entity.setId("1");
        entity.setDescription("some");
        BugReportDto dto = BugReportDto.builder().description(entity.getDescription()).build();

        Optional<BugReport> expected = Optional.of(entity);

        when(repository.findById(entity.getId())).thenReturn(expected);

        Optional<BugReport> actual = Optional.of(service.getBugReportById(expected.get().getId()));

        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(expected, actual),
                () -> verify(repository).findById(entity.getId()));

    }

    @Test
    public void deleteBugReportById() {
        BugReport entity = new BugReport();
        entity.setId("1");
        entity.setDescription("some");

        Optional<BugReport> expected = Optional.of(entity);

        when(repository.findById(entity.getId())).thenReturn(expected);

        assertAll(
                () -> service.deleteBugReportById(entity.getId()));

    }

}
