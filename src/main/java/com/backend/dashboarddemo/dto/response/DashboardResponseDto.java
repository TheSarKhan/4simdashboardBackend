package com.backend.dashboarddemo.dto.response;

public record DashboardResponseDto(Long id, String name, String embedUrl, boolean active) {
}