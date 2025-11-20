package com.backend.dashboarddemo.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequestDto(@NotBlank String email) {
}
