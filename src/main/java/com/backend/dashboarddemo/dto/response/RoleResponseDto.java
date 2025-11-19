package com.backend.dashboarddemo.dto.response;

import com.backend.dashboarddemo.model.Dashboard;

import java.time.Instant;
import java.util.Set;

public record RoleResponseDto(Long id, String name, String description, Instant createdAt, Set<Dashboard> dashboards) {
}
