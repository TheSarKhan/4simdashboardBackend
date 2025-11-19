package com.backend.dashboarddemo.dto.request;

import java.util.Set;

public record UserUpdateRoleRequestDto(Long userId, Set<Long> roleIds) {
}
