package com.backend.dashboarddemo.service;

import com.backend.dashboarddemo.dto.request.UserProfileUpdateRequestDto;
import com.backend.dashboarddemo.dto.response.DashboardResponseDto;
import com.backend.dashboarddemo.dto.response.UserResponseDto;

import java.util.List;

public interface ProfileService {
    UserResponseDto updateProfile(UserProfileUpdateRequestDto userProfileUpdateRequestDto);

    UserResponseDto resetCurrentUserPassword();

    List<DashboardResponseDto> getCurrentUserDashboards();

}
