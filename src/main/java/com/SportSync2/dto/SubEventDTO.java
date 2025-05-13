package com.SportSync2.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubEventDTO {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Sub-event name must be between 3 and 100 characters")
    @Pattern(
            regexp = "^[A-Za-z0-9 ]+$",
            message = "Event name must contain only letters, numbers, and spaces"
    )
    private String name;

    private boolean teamBased;

    @Min(value = 1, message = "At least one participant is required")
    @Max(value = 1000, message = "Participants must be less than 1000")
    private int maxParticipants;

    @NotNull(message = "Ground ID is required")
    private Long groundId;

    @NotNull(message = "Start time is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;
}
