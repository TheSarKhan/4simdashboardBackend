package com.backend.dashboarddemo.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record UserRequestDto(@NotBlank String fullName, @NotBlank String email,
                             @NotBlank String phoneNumber, Set<Long> roleIds) {
}
