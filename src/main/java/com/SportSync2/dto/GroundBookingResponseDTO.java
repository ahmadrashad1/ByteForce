package com.SportSync2.dto;

import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroundBookingResponseDTO {

    @NotNull(message = "Booking ID must not be null")
    private Long id;

    @NotNull(message = "Ground details must not be null")
    private GroundDTO ground;

    @NotNull(message = "Start time is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;
}
