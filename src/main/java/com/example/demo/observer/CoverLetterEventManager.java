package com.example.demo.observer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CoverLetterEventManager {

    private static final CoverLetterEventManager INSTANCE = new CoverLetterEventManager();
    private final List<CoverLetterObserver> observers = new CopyOnWriteArrayList<>();

    private CoverLetterEventManager() {}

    public static CoverLetterEventManager getInstance() {
        return INSTANCE;
    }

    public void addObserver(CoverLetterObserver observer) {
        observers.add(observer);
    }

    public void notifyStarted(Long userId, String company, String role) {
        observers.forEach(o -> o.onGenerationStarted(userId, company, role));
    }

    public void notifyCompleted(Long userId, String company, String role) {
        observers.forEach(o -> o.onGenerationCompleted(userId, company, role));
    }

    public void notifyFailed(Long userId, String company, String role, String reason) {
        observers.forEach(o -> o.onGenerationFailed(userId, company, role, reason));
    }
}
