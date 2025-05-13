package com.SportSync2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotBlank(message = "Event name is required")
    @Size(min = 3, max = 100, message = "Event name must be between 3 and 100 characters")
    private String email;


    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;
}
