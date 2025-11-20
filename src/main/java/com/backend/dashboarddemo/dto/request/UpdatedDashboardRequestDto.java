package com.backend.dashboarddemo.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdatedDashboardRequestDto(@NotBlank String name, @NotBlank String embedUrl, boolean active) {
}
