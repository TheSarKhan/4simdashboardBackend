package com.backend.dashboarddemo.dto.request;

import java.util.Set;

public record RoleRequestDto(String name, String description, Set<Long> dashboardIds) {
}
