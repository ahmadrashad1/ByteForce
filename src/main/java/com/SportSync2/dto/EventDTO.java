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
public class EventDTO {

    private Long id;

    @NotBlank(message = "Event name is required")
    @Size(min = 3, max = 100, message = "Event name must be between 3 and 100 characters")
    @Pattern(
            regexp = "^[A-Za-z0-9 ]+$",
            message = "Event name must contain only letters, numbers, and spaces"
    )
    private String name;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
    @Pattern(
            regexp = ".*[A-Za-z0-9]+.*",
            message = "Description must contain at least one letter or number"
    )
    private String description;

    @NotNull(message = "host user id required")
    private Long createdByUserId;

    @NotNull(message = "Start time is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;
}
