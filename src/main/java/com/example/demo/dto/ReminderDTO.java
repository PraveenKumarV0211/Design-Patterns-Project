package com.jobtracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReminderDTO {
    private Long id;

    @NotNull
    private LocalDate reminderDate;

    @NotBlank
    private String message;

    private boolean isCompleted;
    private Long applicationId;
}