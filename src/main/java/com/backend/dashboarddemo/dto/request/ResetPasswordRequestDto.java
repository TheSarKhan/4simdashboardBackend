package com.backend.dashboarddemo.dto.request;

public record ResetPasswordRequestDto(String resetToken,String password) {
}
