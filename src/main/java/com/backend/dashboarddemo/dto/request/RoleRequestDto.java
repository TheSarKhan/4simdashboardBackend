package com.backend.dashboarddemo.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record RoleRequestDto(@NotBlank String name, @NotBlank String description, Set<Long> dashboardIds) {
}
