package com.example.demo.controller;

import com.example.demo.observer.CoverLetterNotificationObserver;
import com.example.demo.pattern.facade.CoverLetterFacade;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
public class CoverLetterController {

    private final CoverLetterFacade coverLetterFacade;
    private final CoverLetterNotificationObserver notificationObserver;

    public CoverLetterController(CoverLetterFacade coverLetterFacade,
                                  CoverLetterNotificationObserver notificationObserver) {
        this.coverLetterFacade = coverLetterFacade;
        this.notificationObserver = notificationObserver;
    }

    @PostMapping("/{applicationId}/cover-letter")
    public ResponseEntity<byte[]> generateCoverLetter(
            @PathVariable Long applicationId,
            @RequestParam Long userId) {

        byte[] pdf = coverLetterFacade.generateCoverLetterPdf(userId, applicationId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=cover-letter.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    // Frontend polls this to get and clear pending notifications
    @GetMapping("/notifications")
    public ResponseEntity<List<Map<String, String>>> getNotifications(@RequestParam Long userId) {
        return ResponseEntity.ok(notificationObserver.popNotifications(userId));
    }
}
