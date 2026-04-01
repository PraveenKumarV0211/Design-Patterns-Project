package com.example.demo.service;

import com.example.demo.dto.ApplicationDTO;
import com.example.demo.dto.DashboardDTO;
import com.example.demo.enums.ApplicationStatus;
import com.example.demo.enums.JobType;
import com.example.demo.exception.InvalidStateTransitionException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Application;
import com.example.demo.model.User;
import com.example.demo.observer.DashboardObserver;
import com.example.demo.observer.NotificationObserver;
import com.example.demo.pattern.builder.ApplicationBuilder;
import com.example.demo.pattern.decorator.*;
import com.example.demo.pattern.singleton.ApplicationManager;
import com.example.demo.pattern.state.ApplicationState;
import com.example.demo.pattern.state.StateContext;
import com.example.demo.pattern.strategy.*;
import com.example.demo.repository.ApplicationRepository;
import com.example.demo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    public ApplicationService(ApplicationRepository applicationRepository, UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void initObservers() {
        ApplicationManager manager = ApplicationManager.getInstance();
        manager.addObserver(new DashboardObserver());
        manager.addObserver(new NotificationObserver());
    }

    // CREATE — uses Builder pattern
    public Application createApplication(Long userId, ApplicationDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Application app = new ApplicationBuilder()
                .withCompanyName(dto.getCompanyName())
                .withRoleName(dto.getRoleName())
                .withJobType(dto.getJobType())
                .withApplicationDate(dto.getApplicationDate() != null ? dto.getApplicationDate() : LocalDate.now())
                .withNotes(dto.getNotes())
                .withUser(user)
                .build();

        // Apply decorators
        if (dto.getReferralInfo() != null) {
            new ReferralDecorator(app, dto.getReferralInfo());
        }
        if (dto.getSalaryDetails() != null) {
            new SalaryDecorator(app, dto.getSalaryDetails());
        }
        if (dto.getInterviewNotes() != null) {
            new InterviewNotesDecorator(app, dto.getInterviewNotes());
        }

        Application saved = applicationRepository.save(app);
        ApplicationManager.getInstance().notifyObservers(saved);
        return saved;
    }

    // READ
    public List<Application> getApplicationsByUser(Long userId) {
        return applicationRepository.findByUserId(userId);
    }

    public Application getApplicationById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
    }

    // UPDATE
    public Application updateApplication(Long id, ApplicationDTO dto) {
        Application app = getApplicationById(id);
        app.setCompanyName(dto.getCompanyName());
        app.setRoleName(dto.getRoleName());
        app.setJobType(dto.getJobType());
        app.setNotes(dto.getNotes());
        app.setReferralInfo(dto.getReferralInfo());
        app.setSalaryDetails(dto.getSalaryDetails());
        app.setInterviewNotes(dto.getInterviewNotes());
        return applicationRepository.save(app);
    }

    // DELETE
    public void deleteApplication(Long id) {
        Application app = getApplicationById(id);
        applicationRepository.delete(app);
    }

    // STATE TRANSITION — uses State pattern
    public Application transitionState(Long id, String action) {
        Application app = getApplicationById(id);
        ApplicationState state = StateContext.getState(app.getStatus());

        try {
            switch (action.toUpperCase()) {
                case "NEXT" -> state.handleNext(app);
                case "REJECT" -> state.handleRejected(app);
                case "WITHDRAW" -> state.handleWithdrawn(app);
                default -> throw new IllegalArgumentException("Invalid action: " + action);
            }
        } catch (IllegalStateException e) {
            throw new InvalidStateTransitionException(e.getMessage());
        }

        Application saved = applicationRepository.save(app);
        ApplicationManager.getInstance().notifyObservers(saved);
        return saved;
    }

    // SORT — uses Strategy pattern
    public List<Application> getSortedApplications(Long userId, String sortBy) {
        List<Application> apps = applicationRepository.findByUserId(userId);
        SortStrategy strategy = switch (sortBy.toLowerCase()) {
            case "company" -> new CompanySortStrategy();
            default -> new DateSortStrategy();
        };
        return strategy.sort(apps);
    }

    // FILTER
    public List<Application> filterByStatus(Long userId, ApplicationStatus status) {
        return applicationRepository.findByUserIdAndStatus(userId, status);
    }

    public List<Application> filterByJobType(Long userId, JobType jobType) {
        return applicationRepository.findByUserIdAndJobType(userId, jobType);
    }

    public List<Application> searchByCompany(Long userId, String companyName) {
        return applicationRepository.findByUserIdAndCompanyNameContainingIgnoreCase(userId, companyName);
    }

    // DASHBOARD
    public DashboardDTO getDashboard(Long userId) {
        List<Application> apps = applicationRepository.findByUserId(userId);
        long total = apps.size();

        Map<String, Long> statusBreakdown = apps.stream()
                .collect(Collectors.groupingBy(a -> a.getStatus().name(), Collectors.counting()));

        long offers = apps.stream().filter(a -> a.getStatus() == ApplicationStatus.OFFER).count();
        double successRate = total > 0 ? (double) offers / total * 100 : 0;

        Map<String, Long> overTime = apps.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getApplicationDate().toString().substring(0, 7),
                        TreeMap::new,
                        Collectors.counting()));

        DashboardDTO dto = new DashboardDTO();
        dto.setTotalApplications(total);
        dto.setStatusBreakdown(statusBreakdown);
        dto.setSuccessRate(Math.round(successRate * 100.0) / 100.0);
        dto.setApplicationsOverTime(overTime);
        return dto;
    }
}