package com.example.demo.observer;

public interface CoverLetterObserver {
    void onGenerationStarted(Long userId, String companyName, String roleName);
    void onGenerationCompleted(Long userId, String companyName, String roleName);
    void onGenerationFailed(Long userId, String companyName, String roleName, String reason);
}
