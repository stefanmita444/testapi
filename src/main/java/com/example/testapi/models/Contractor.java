package com.example.testapi.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "contractors")
public class Contractor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String jobCode;

    @NotNull
    private String description;

    @NotNull
    private double hourlyRate;

    @NotNull
    private int maxHoursPerDay;

    public Contractor() {
    }

    public Contractor(String jobCode, String description, double hourlyRate, int maxHoursPerDay) {
        this.jobCode = jobCode;
        this.description = description;
        this.hourlyRate = hourlyRate;
        this.maxHoursPerDay = maxHoursPerDay;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public void setMaxHoursPerDay(int maxHoursPerDay) {
        this.maxHoursPerDay = maxHoursPerDay;
    }

    public Long getId() {
        return id;
    }

    public String getJobCode() {
        return jobCode;
    }

    public String getDescription() {
        return description;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public int getMaxHoursPerDay() {
        return maxHoursPerDay;
    }

}
