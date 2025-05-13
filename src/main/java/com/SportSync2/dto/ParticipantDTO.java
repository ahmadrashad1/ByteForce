package com.SportSync2.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipantDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Sub-event ID is required")
    private Long subEventId;

    private Long teamId;

    @Size(min = 3, max = 100, message = "Team name must be between 3 and 100 characters")
    @Pattern(
            regexp = "^[A-Za-z0-9 ]+$",
            message = "Event name must contain only letters, numbers, and spaces"
    )
    private String teamName;

    @Size(max = 15, message = "Maximum 10 team members allowed")
    private List<@Valid TeamMemberDTO> teamMembers;
}
