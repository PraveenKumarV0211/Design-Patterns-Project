package com.example.demo.pattern.facade;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Application;
import com.example.demo.model.User;
import com.example.demo.observer.CoverLetterEventManager;
import com.example.demo.observer.CoverLetterNotificationObserver;
import com.example.demo.pattern.template.CoverLetterPromptTemplate;
import com.example.demo.repository.ApplicationRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.PdfGenerationService;
import com.example.demo.strategy.CoverLetterGenerationStrategy;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 * Facade Pattern — single method hides:
 *   1. Fetching user resume
 *   2. Building the LLM prompt (Template Method Pattern)
 *   3. Calling the LLM (Strategy Pattern)
 *   4. Generating the PDF
 *   5. Notifying observers of progress (Observer Pattern)
 */
@Component
public class CoverLetterFacade {

    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final CoverLetterGenerationStrategy generationStrategy;
    private final CoverLetterPromptTemplate promptTemplate;
    private final PdfGenerationService pdfGenerationService;
    private final CoverLetterNotificationObserver notificationObserver;

    public CoverLetterFacade(UserRepository userRepository,
                              ApplicationRepository applicationRepository,
                              CoverLetterGenerationStrategy generationStrategy,
                              CoverLetterPromptTemplate promptTemplate,
                              PdfGenerationService pdfGenerationService,
                              CoverLetterNotificationObserver notificationObserver) {
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.generationStrategy = generationStrategy;
        this.promptTemplate = promptTemplate;
        this.pdfGenerationService = pdfGenerationService;
        this.notificationObserver = notificationObserver;
    }

    @PostConstruct
    public void registerObservers() {
        CoverLetterEventManager.getInstance().addObserver(notificationObserver);
    }

    public byte[] generateCoverLetterPdf(Long userId, Long applicationId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        if (user.getResumeText() == null || user.getResumeText().isBlank()) {
            throw new IllegalStateException("Please upload your resume before generating a cover letter.");
        }

        CoverLetterEventManager events = CoverLetterEventManager.getInstance();
        events.notifyStarted(userId, app.getCompanyName(), app.getRoleName());

        try {
            String prompt = promptTemplate.buildPrompt(
                    user.getResumeText(),
                    app.getCompanyName(),
                    app.getRoleName(),
                    app.getJobType().name(),
                    app.getNotes()
            );

            String coverLetterText = generationStrategy.generate(prompt);
            byte[] pdf = pdfGenerationService.generateCoverLetterPdf(coverLetterText, app.getCompanyName(), app.getRoleName());

            events.notifyCompleted(userId, app.getCompanyName(), app.getRoleName());
            return pdf;
        } catch (Exception e) {
            events.notifyFailed(userId, app.getCompanyName(), app.getRoleName(), e.getMessage());
            throw e;
        }
    }
}
