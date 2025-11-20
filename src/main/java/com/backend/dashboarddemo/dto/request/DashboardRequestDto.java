package com.backend.dashboarddemo.dto.request;

import jakarta.validation.constraints.NotBlank;

public record DashboardRequestDto(@NotBlank String name, @NotBlank  String embedUrl) {
}
