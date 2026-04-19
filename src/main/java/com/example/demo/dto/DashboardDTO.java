package com.example.demo.dto;

import lombok.*;
import java.util.Map;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DashboardDTO {
    private long totalApplications;
    private Map<String, Long> statusBreakdown;
    private double successRate;
    private Map<String, Long> applicationsOverTime;
    private Map<String, Long> applicationsByType;
}