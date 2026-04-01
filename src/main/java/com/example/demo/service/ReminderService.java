package com.jobtracker.service;

import com.jobtracker.dto.ReminderDTO;
import com.jobtracker.exception.ResourceNotFoundException;
import com.jobtracker.model.Application;
import com.jobtracker.model.Reminder;
import com.jobtracker.repository.ApplicationRepository;
import com.jobtracker.repository.ReminderRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final ApplicationRepository applicationRepository;

    public ReminderService(ReminderRepository reminderRepository, ApplicationRepository applicationRepository) {
        this.reminderRepository = reminderRepository;
        this.applicationRepository = applicationRepository;
    }

    public Reminder createReminder(ReminderDTO dto) {
        Application app = applicationRepository.findById(dto.getApplicationId())
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        Reminder reminder = new Reminder();
        reminder.setReminderDate(dto.getReminderDate());
        reminder.setMessage(dto.getMessage());
        reminder.setCompleted(false);
        reminder.setApplication(app);
        return reminderRepository.save(reminder);
    }

    public List<Reminder> getRemindersByApplication(Long applicationId) {
        return reminderRepository.findByApplicationId(applicationId);
    }

    public List<Reminder> getPendingReminders(Long userId) {
        return reminderRepository.findByApplicationUserIdAndIsCompletedFalse(userId);
    }

    public List<Reminder> getDueReminders(Long userId) {
        return reminderRepository.findByApplicationUserIdAndReminderDateLessThanEqualAndIsCompletedFalse(
                userId, LocalDate.now());
    }

    public Reminder completeReminder(Long id) {
        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reminder not found"));
        reminder.setCompleted(true);
        return reminderRepository.save(reminder);
    }

    public void deleteReminder(Long id) {
        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reminder not found"));
        reminderRepository.delete(reminder);
    }
}