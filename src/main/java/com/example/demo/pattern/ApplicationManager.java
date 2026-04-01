package com.example.demo.pattern;

import com.example.demo.model.Application;
import com.example.demo.observer.ApplicationObserver;
import java.util.ArrayList;
import java.util.List;

public class ApplicationManager {

    private static volatile ApplicationManager instance;
    private final List<ApplicationObserver> observers = new ArrayList<>();

    private ApplicationManager() {}

    public static ApplicationManager getInstance() {
        if (instance == null) {
            synchronized (ApplicationManager.class) {
                if (instance == null) {
                    instance = new ApplicationManager();
                }
            }
        }
        return instance;
    }

    public void addObserver(ApplicationObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ApplicationObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Application app) {
        for (ApplicationObserver observer : observers) {
            observer.onStateChanged(app);
        }
    }
}