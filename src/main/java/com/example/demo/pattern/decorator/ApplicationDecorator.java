package com.example.demo.pattern.decorator;

import com.example.demo.model.Application;

public abstract class ApplicationDecorator {
    protected Application wrapped;

    public ApplicationDecorator(Application wrapped) {
        this.wrapped = wrapped;
    }

    public abstract String getDetails();

    public Application getWrapped() {
        return wrapped;
    }
}
