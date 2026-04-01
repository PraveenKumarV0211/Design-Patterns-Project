package com.example.demo.pattern.state;


import com.example.demo.enums.ApplicationStatus;

public class StateContext {

    public static ApplicationState getState(ApplicationStatus status) {
        return switch (status) {
            case APPLIED -> new AppliedState();
            case OA -> new OAState();
            case INTERVIEW -> new InterviewState();
            case OFFER -> new OfferState();
            case REJECTED -> new RejectedState();
            case WITHDRAWN -> new WithdrawnState();
        };
    }
}