package com.example.demo.pattern.state;



import com.example.demo.model.Application;

public class RejectedState implements ApplicationState {
    @Override
    public void handleNext(Application app) {
        throw new IllegalStateException("No further transitions from REJECTED.");
    }

    @Override
    public void handleRejected(Application app) {
        throw new IllegalStateException("Already REJECTED.");
    }

    @Override
    public void handleWithdrawn(Application app) {
        throw new IllegalStateException("Cannot withdraw a rejected application.");
    }

    @Override
    public String getStateName() { return "REJECTED"; }
}