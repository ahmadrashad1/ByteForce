package com.SportSync2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroundDTO {

    private Long id;

    @NotBlank(message = "Ground name is required")
    @Size(min = 3, max = 100, message = "Ground name must be between 3 and 100 characters")
    @Pattern(
            regexp = "^[A-Za-z0-9 ]+$",
            message = "Ground name must contain only letters, numbers, and spaces"
    )
    private String name;

    @NotBlank(message = "Location is required")
    @Size(min = 5, max = 150, message = "Location must be between 5 and 150 characters")
    @Pattern(
            regexp = "^[A-Za-z0-9 ,.-]+$",
            message = "Location can contain letters, numbers, commas, periods, hyphens, and spaces"
    )
    private String location;
}
