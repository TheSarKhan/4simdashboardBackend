package com.backend.dashboarddemo.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserProfileUpdateRequestDto(@NotBlank String fullName,
                                          @NotBlank String email,
                                          @NotBlank String phoneNumber) {
}
