package com.example.demo.pattern.state;

import com.example.demo.model.Application;

public class OfferState implements ApplicationState {
    @Override
    public void handleNext(Application app) {
        throw new IllegalStateException("No further transitions from OFFER.");
    }

    @Override
    public void handleRejected(Application app) {
        throw new IllegalStateException("Cannot reject an application in OFFER state.");
    }

    @Override
    public void handleWithdrawn(Application app) {
        throw new IllegalStateException("Cannot withdraw an application in OFFER state.");
    }

    @Override
    public String getStateName() { return "OFFER"; }
}
