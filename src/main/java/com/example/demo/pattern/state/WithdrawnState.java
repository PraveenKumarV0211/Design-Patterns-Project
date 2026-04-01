package com.example.demo.pattern.state;



import com.example.demo.model.Application;

public class WithdrawnState implements ApplicationState {
    @Override
    public void handleNext(Application app) {
        throw new IllegalStateException("No further transitions from WITHDRAWN.");
    }

    @Override
    public void handleRejected(Application app) {
        throw new IllegalStateException("Cannot reject a withdrawn application.");
    }

    @Override
    public void handleWithdrawn(Application app) {
        throw new IllegalStateException("Already WITHDRAWN.");
    }

    @Override
    public String getStateName() { return "WITHDRAWN"; }
}