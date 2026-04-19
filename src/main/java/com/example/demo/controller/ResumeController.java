package com.example.demo.controller;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class ResumeController {

    private final UserRepository userRepository;

    public ResumeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/{userId}/resume/upload")
    public ResponseEntity<Map<String, String>> uploadResumePdf(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) throws Exception {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String text;
        String contentType = file.getContentType();
        if ("application/pdf".equals(contentType)) {
            try (PDDocument doc = PDDocument.load(file.getInputStream())) {
                text = new PDFTextStripper().getText(doc);
            }
        } else {
            text = new String(file.getBytes());
        }

        user.setResumeText(text);
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "Resume uploaded successfully"));
    }

    @PutMapping("/{userId}/resume")
    public ResponseEntity<Map<String, String>> saveResumeText(
            @PathVariable Long userId,
            @RequestBody Map<String, String> body) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setResumeText(body.get("resumeText"));
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "Resume saved successfully"));
    }

    @GetMapping("/{userId}/resume")
    public ResponseEntity<Map<String, String>> getResume(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return ResponseEntity.ok(Map.of("resumeText", user.getResumeText() != null ? user.getResumeText() : ""));
    }
}
