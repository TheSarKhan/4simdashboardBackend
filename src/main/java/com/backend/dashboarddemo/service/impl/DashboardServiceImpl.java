package com.backend.dashboarddemo.service.impl;

import com.backend.dashboarddemo.dto.request.DashboardRequestDto;
import com.backend.dashboarddemo.dto.request.UpdatedDashboardRequestDto;
import com.backend.dashboarddemo.dto.response.DashboardResponseDto;
import com.backend.dashboarddemo.handler.exception.DataNotFoundException;
import com.backend.dashboarddemo.mapper.DashboardMapper;
import com.backend.dashboarddemo.model.Dashboard;
import com.backend.dashboarddemo.repository.DashboardRepository;
import com.backend.dashboarddemo.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final DashboardRepository dashboardRepository;
    private final DashboardMapper dashboardMapper;


    @Override
    public DashboardResponseDto getById(Long id) {
        return dashboardMapper.dashbordToDashboardResponseDto(dashboardRepository.findById(id).orElseThrow(() -> {
            log.error("Dashboard not found with id: {}", id);
            return new DataNotFoundException("Dashboard not found with id: " + id);
        }));
    }

    @Override
    public List<DashboardResponseDto> getAll() {
        return dashboardMapper.dashboardsToDashboardResponseDtos(dashboardRepository.findAll());
    }

    @Override
    public DashboardResponseDto add(DashboardRequestDto dashboardRequestDto) {
        Dashboard dashboard = dashboardMapper.dashboardRequestToDashboard(dashboardRequestDto);
        Dashboard saved = dashboardRepository.save(dashboard);
        return dashboardMapper.dashbordToDashboardResponseDto(saved);
    }

    @Override
    public DashboardResponseDto update(Long id, UpdatedDashboardRequestDto updatedDashboardRequestDto) {
        Dashboard dashboard = dashboardRepository.findById(id).orElseThrow(() -> {
            log.error("Dashboard not found with id: {}", id);
            return new DataNotFoundException("Dashboard not found with id: " + id);
        });
        dashboardMapper.updateDashboardFromDashboardRequest(updatedDashboardRequestDto, dashboard);

        Dashboard saved = dashboardRepository.save(dashboard);
        return dashboardMapper.dashbordToDashboardResponseDto(saved);
    }

    @Override
    public void delete(Long id) {
        dashboardRepository.deleteById(id);
        log.info("Dashboard with id {} deleted", id);
    }
}
