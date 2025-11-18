package com.backend.dashboarddemo.service;

import com.backend.dashboarddemo.dto.request.DashboardRequestDto;
import com.backend.dashboarddemo.dto.request.UpdatedDashboardRequestDto;
import com.backend.dashboarddemo.dto.response.DashboardResponseDto;

import java.util.List;

public interface DashboardService {

    DashboardResponseDto getById(Long id);

    List<DashboardResponseDto> getAll();

    DashboardResponseDto add(DashboardRequestDto dashboardRequestDto);

    DashboardResponseDto update(Long id, UpdatedDashboardRequestDto updatedDashboardRequestDto);

    void delete(Long id);
}
