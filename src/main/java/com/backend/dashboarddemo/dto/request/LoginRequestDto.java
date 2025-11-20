package com.backend.dashboarddemo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(@NotBlank @Email(message = "Email format is not valid")
                              String email, @NotBlank String password) {
}
