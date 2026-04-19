package com.example.demo.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Component
public class CoverLetterNotificationObserver implements CoverLetterObserver {

    private static final Logger log = LoggerFactory.getLogger(CoverLetterNotificationObserver.class);

    // userId → list of notification messages
    private final ConcurrentHashMap<Long, CopyOnWriteArrayList<Map<String, String>>> store = new ConcurrentHashMap<>();

    @Override
    public void onGenerationStarted(Long userId, String company, String role) {
        log.info("[CoverLetter] Started: userId={} role={} company={}", userId, role, company);
        push(userId, "info", "Generating cover letter for " + role + " at " + company + "...");
    }

    @Override
    public void onGenerationCompleted(Long userId, String company, String role) {
        log.info("[CoverLetter] Completed: userId={} role={} company={}", userId, role, company);
        push(userId, "success", "Cover letter for " + role + " at " + company + " is ready!");
    }

    @Override
    public void onGenerationFailed(Long userId, String company, String role, String reason) {
        log.error("[CoverLetter] Failed: userId={} reason={}", userId, reason);
        push(userId, "error", "Failed to generate cover letter for " + role + " at " + company + ".");
    }

    public List<Map<String, String>> popNotifications(Long userId) {
        List<Map<String, String>> notifications = store.getOrDefault(userId, new CopyOnWriteArrayList<>());
        List<Map<String, String>> copy = new ArrayList<>(notifications);
        store.remove(userId);
        return copy;
    }

    private void push(Long userId, String type, String message) {
        store.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>())
             .add(Map.of("type", type, "message", message, "time", LocalDateTime.now().toString()));
    }
}
