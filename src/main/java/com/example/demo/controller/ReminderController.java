package com.example.demo.controller;

import com.example.demo.dto.ReminderDTO;
import com.example.demo.model.Reminder;
import com.example.demo.service.ReminderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reminders")
public class ReminderController {

    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @PostMapping
    public ResponseEntity<Reminder> create(@Valid @RequestBody ReminderDTO dto) {
        return ResponseEntity.ok(reminderService.createReminder(dto));
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<List<Reminder>> getByApplication(@PathVariable Long applicationId) {
        return ResponseEntity.ok(reminderService.getRemindersByApplication(applicationId));
    }

    @GetMapping("/user/{userId}/pending")
    public ResponseEntity<List<Reminder>> getPending(@PathVariable Long userId) {
        return ResponseEntity.ok(reminderService.getPendingReminders(userId));
    }

    @GetMapping("/user/{userId}/due")
    public ResponseEntity<List<Reminder>> getDue(@PathVariable Long userId) {
        return ResponseEntity.ok(reminderService.getDueReminders(userId));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Reminder> complete(@PathVariable Long id) {
        return ResponseEntity.ok(reminderService.completeReminder(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        reminderService.deleteReminder(id);
        return ResponseEntity.ok(Map.of("message", "Reminder deleted successfully"));
    }
}
