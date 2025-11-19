package com.backend.dashboarddemo.dto.response;

import java.time.Instant;
import java.util.Set;

public record UserResponseDto(Long id,
                              String fullName,
                              String email,
                              String phoneNumber,
                              boolean active,
                              Instant createdAt,
                              Set<String> roles) {
}
