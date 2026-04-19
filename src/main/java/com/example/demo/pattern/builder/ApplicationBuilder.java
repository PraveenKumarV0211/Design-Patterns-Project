package com.example.demo.pattern.builder;

import com.example.demo.enums.ApplicationStatus;
import com.example.demo.enums.JobType;
import com.example.demo.model.Application;
import com.example.demo.model.User;
import java.time.LocalDate;

public class ApplicationBuilder {

    private final Application application;

    public ApplicationBuilder() {
        this.application = new Application();
        this.application.setStatus(ApplicationStatus.APPLIED);
        this.application.setApplicationDate(LocalDate.now());
    }

    public ApplicationBuilder withCompanyName(String companyName) {
        application.setCompanyName(companyName);
        return this;
    }

    public ApplicationBuilder withRoleName(String roleName) {
        application.setRoleName(roleName);
        return this;
    }

    public ApplicationBuilder withJobType(JobType jobType) {
        application.setJobType(jobType);
        return this;
    }

    public ApplicationBuilder withApplicationDate(LocalDate date) {
        application.setApplicationDate(date);
        return this;
    }

    public ApplicationBuilder withNotes(String notes) {
        application.setNotes(notes);
        return this;
    }

    public ApplicationBuilder withUser(User user) {
        application.setUser(user);
        return this;
    }

    public ApplicationBuilder withReferralInfo(String referralInfo) {
        application.setReferralInfo(referralInfo);
        return this;
    }

    public ApplicationBuilder withSalaryDetails(Double salary) {
        application.setSalaryDetails(salary);
        return this;
    }

    public ApplicationBuilder withInterviewNotes(String interviewNotes) {
        application.setInterviewNotes(interviewNotes);
        return this;
    }

    public Application build() {
        if (application.getCompanyName() == null || application.getRoleName() == null || application.getJobType() == null) {
            throw new IllegalStateException("Company name, role name, and job type are required.");
        }
        return application;
    }
}