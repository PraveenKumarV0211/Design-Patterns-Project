package com.example.demo.pattern.state;



import com.example.demo.enums.ApplicationStatus;
import com.example.demo.model.Application;

public class AppliedState implements ApplicationState {
    @Override
    public void handleNext(Application app) { app.setStatus(ApplicationStatus.OA); }

    @Override
    public void handleRejected(Application app) { app.setStatus(ApplicationStatus.REJECTED); }

    @Override
    public void handleWithdrawn(Application app) { app.setStatus(ApplicationStatus.WITHDRAWN); }

    @Override
    public String getStateName() { return "APPLIED"; }
}