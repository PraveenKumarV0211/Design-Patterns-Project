package com.example.demo.pattern;


import com.example.demo.enums.ApplicationStatus;
import com.example.demo.enums.JobType;
import com.example.demo.model.Application;
import java.time.LocalDate;

public class ApplicationFactory {

    public static Application create(JobType type) {
        Application app = new Application();
        app.setJobType(type);
        app.setStatus(ApplicationStatus.APPLIED);
        app.setApplicationDate(LocalDate.now());

        switch (type) {
            case FULLTIME -> app.setNotes("Full-time position");
            case INTERNSHIP -> app.setNotes("Internship position");
            case CONTRACT -> app.setNotes("Contract position");
        }
        return app;
    }
}