package com.example.AuthService.dto;

import com.example.AuthService.model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Full name is required")
    @Size(min = 3, max = 100, message = "Full name must be between 3 and 50 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Size(min = 3, max = 128, message = "Email must be between 3 and 10 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 3, message = "Password must be at least 3 characters")
    private String password;

    private Role role;
}
