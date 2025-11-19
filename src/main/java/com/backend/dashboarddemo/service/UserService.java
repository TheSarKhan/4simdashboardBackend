package com.backend.dashboarddemo.service;

import com.backend.dashboarddemo.dto.request.UserRequestDto;
import com.backend.dashboarddemo.dto.request.UserResetPasswordRequestDto;
import com.backend.dashboarddemo.dto.response.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto getById(Long id);

    List<UserResponseDto> getAll();

    UserResponseDto create(UserRequestDto userRequestDto);

    UserResponseDto resetPassword(UserResetPasswordRequestDto userResetPasswordRequestDto);

    UserResponseDto update(Long id, UserRequestDto userRequestDto);

    void delete(Long id);
}
