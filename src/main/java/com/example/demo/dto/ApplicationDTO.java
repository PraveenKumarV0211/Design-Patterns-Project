package com.jobtracker.dto;

import com.jobtracker.enums.ApplicationStatus;
import com.jobtracker.enums.JobType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ApplicationDTO {
    private Long id;

    @NotBlank
    private String companyName;

    @NotBlank
    private String roleName;

    @NotNull
    private JobType jobType;

    private LocalDate applicationDate;
    private ApplicationStatus status;
    private String notes;
}