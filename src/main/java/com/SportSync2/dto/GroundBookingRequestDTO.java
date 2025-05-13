package com.SportSync2.dto;

import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroundBookingRequestDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Ground ID is required")
    private Long groundId;

    @NotNull(message = "Start time is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;
}
