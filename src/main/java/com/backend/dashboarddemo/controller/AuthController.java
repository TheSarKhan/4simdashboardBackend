package com.backend.dashboarddemo.controller;

import com.backend.dashboarddemo.dto.request.ForgotPasswordRequestDto;
import com.backend.dashboarddemo.dto.request.LoginRequestDto;
import com.backend.dashboarddemo.dto.request.ResetPasswordRequestDto;
import com.backend.dashboarddemo.dto.request.VerifyCodeRequestDto;
import com.backend.dashboarddemo.dto.response.LoginResponseDto;
import com.backend.dashboarddemo.dto.response.MessageResponse;
import com.backend.dashboarddemo.dto.response.VerifyCodeResponseDto;
import com.backend.dashboarddemo.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/forgot")
    public ResponseEntity<MessageResponse> forgotPassword(
            @RequestBody ForgotPasswordRequestDto request
    ) {
        return ResponseEntity.ok(authService.forgotPassword(request));
    }

    @PostMapping("/verify-code")
    public ResponseEntity<VerifyCodeResponseDto> verifyCode(
            @RequestBody VerifyCodeRequestDto request
    ) {
        return ResponseEntity.ok(authService.verifyCode(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(
            @RequestBody ResetPasswordRequestDto request
    ) {
        return ResponseEntity.ok(authService.resetPassword(request));
    }
}
