package com.backend.dashboarddemo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.Set;

public record UserRequestDto(@NotBlank String fullName,
                             @NotBlank @Email(message = "Email format is not valid") String email,
                             @NotBlank @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Invalid phone number format")
                             String phoneNumber,
                             Set<Long> roleIds) {
}
