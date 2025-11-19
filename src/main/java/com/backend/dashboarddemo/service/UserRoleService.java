package com.backend.dashboarddemo.service;

import com.backend.dashboarddemo.dto.request.RoleRequestDto;
import com.backend.dashboarddemo.dto.response.RoleResponseDto;

import java.util.List;

public interface UserRoleService {
    RoleResponseDto create(RoleRequestDto requestDto);

    List<RoleResponseDto> getAll();

    RoleResponseDto getById(Long id);

    void delete(Long id);

    RoleResponseDto update(Long id, RoleRequestDto requestDto);

    boolean hasAccessToDashboard(Long dashboardId, Long userId);
}
