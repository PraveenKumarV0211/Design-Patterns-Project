package com.example.demo.pattern.state;


import com.example.demo.model.Application;

public interface ApplicationState {
    void handleNext(Application app);
    void handleRejected(Application app);
    void handleWithdrawn(Application app);
    String getStateName();
}