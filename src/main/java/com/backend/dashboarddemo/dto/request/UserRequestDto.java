package com.backend.dashboarddemo.dto.request;

import java.util.Set;

public record UserRequestDto(String fullName, String email, String password,
                             String phoneNumber, Set<Long> roleIds) {
}
