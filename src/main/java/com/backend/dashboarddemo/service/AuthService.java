package com.backend.dashboarddemo.service;

import com.backend.dashboarddemo.dto.request.ForgotPasswordRequestDto;
import com.backend.dashboarddemo.dto.request.LoginRequestDto;
import com.backend.dashboarddemo.dto.request.ResetPasswordRequestDto;
import com.backend.dashboarddemo.dto.response.LoginResponseDto;
import com.backend.dashboarddemo.dto.response.MessageResponse;

public interface AuthService {

    LoginResponseDto login(LoginRequestDto request);

    MessageResponse forgotPassword(ForgotPasswordRequestDto request);

    MessageResponse resetPassword(ResetPasswordRequestDto request);
}
