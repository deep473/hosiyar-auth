package com.auth.assessment.dto;

import com.auth.assessment.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Please provide a valid email")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 5, message = "Password must have at least 5 characters")
        String password,

        @NotNull(message = "Role is required")
        Role role
) {
}
