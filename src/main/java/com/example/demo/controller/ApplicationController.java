package com.example.demo.controller;

import com.example.demo.dto.ApplicationDTO;
import com.example.demo.dto.DashboardDTO;
import com.example.demo.enums.ApplicationStatus;
import com.example.demo.enums.JobType;
import com.example.demo.model.Application;
import com.example.demo.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<Application> create(@PathVariable Long userId,
                                               @Valid @RequestBody ApplicationDTO dto) {
        return ResponseEntity.ok(applicationService.createApplication(userId, dto));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Application>> getAll(@PathVariable Long userId) {
        return ResponseEntity.ok(applicationService.getApplicationsByUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Application> getById(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.getApplicationById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Application> update(@PathVariable Long id,
                                               @Valid @RequestBody ApplicationDTO dto) {
        return ResponseEntity.ok(applicationService.updateApplication(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.ok(Map.of("message", "Application deleted successfully"));
    }

    @PutMapping("/{id}/transition")
    public ResponseEntity<Application> transition(@PathVariable Long id,
                                                   @RequestParam String action) {
        return ResponseEntity.ok(applicationService.transitionState(id, action));
    }

    @GetMapping("/user/{userId}/sort")
    public ResponseEntity<List<Application>> sorted(@PathVariable Long userId,
                                                     @RequestParam(defaultValue = "date") String sortBy) {
        return ResponseEntity.ok(applicationService.getSortedApplications(userId, sortBy));
    }

    @GetMapping("/user/{userId}/filter/status")
    public ResponseEntity<List<Application>> filterByStatus(@PathVariable Long userId,
                                                             @RequestParam ApplicationStatus status) {
        return ResponseEntity.ok(applicationService.filterByStatus(userId, status));
    }

    @GetMapping("/user/{userId}/filter/type")
    public ResponseEntity<List<Application>> filterByType(@PathVariable Long userId,
                                                           @RequestParam JobType jobType) {
        return ResponseEntity.ok(applicationService.filterByJobType(userId, jobType));
    }

    @GetMapping("/user/{userId}/search")
    public ResponseEntity<List<Application>> search(@PathVariable Long userId,
                                                     @RequestParam String company) {
        return ResponseEntity.ok(applicationService.searchByCompany(userId, company));
    }

    @GetMapping("/user/{userId}/dashboard")
    public ResponseEntity<DashboardDTO> dashboard(@PathVariable Long userId) {
        return ResponseEntity.ok(applicationService.getDashboard(userId));
    }
}
