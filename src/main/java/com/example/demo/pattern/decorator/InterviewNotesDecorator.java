package com.example.demo.pattern.decorator;

import com.example.demo.model.Application;

public class InterviewNotesDecorator extends ApplicationDecorator {

    private final String interviewNotes;

    public InterviewNotesDecorator(Application wrapped, String interviewNotes) {
        super(wrapped);
        this.interviewNotes = interviewNotes;
        wrapped.setInterviewNotes(interviewNotes);
    }

    @Override
    public String getDetails() {
        return "Interview Notes: " + interviewNotes;
    }
}
