package com.backend.dashboarddemo.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequestDto(@NotBlank String email, @NotBlank String code) {
}
