package com.backend.dashboarddemo.service.impl;

import com.backend.dashboarddemo.dto.request.DashboardRequestDto;
import com.backend.dashboarddemo.dto.request.UpdatedDashboardRequestDto;
import com.backend.dashboarddemo.dto.response.DashboardResponseDto;
import com.backend.dashboarddemo.handler.exception.DataNotFoundException;
import com.backend.dashboarddemo.mapper.DashboardMapper;
import com.backend.dashboarddemo.model.Dashboard;
import com.backend.dashboarddemo.model.User;
import com.backend.dashboarddemo.model.UserRole;
import com.backend.dashboarddemo.repository.DashboardRepository;
import com.backend.dashboarddemo.repository.UserRepository;
import com.backend.dashboarddemo.repository.UserRoleRepository;
import com.backend.dashboarddemo.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final DashboardRepository dashboardRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
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

    @Override
    @Transactional(readOnly = true)
    public List<DashboardResponseDto> getDashboardsForUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("User not found with id: {}", userId);
            return new DataNotFoundException("User not found with id: " + userId);
        });
        Set<Dashboard> dashboards = user.getRoles().stream()
                .flatMap(role -> role.getDashboards().stream())
                .filter(Dashboard::isActive)
                .collect(Collectors.toSet());
        return dashboardMapper.dashboardsToDashboardResponseDtos(dashboards.stream().toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DashboardResponseDto> getDashboardsByRole(Long roleId) {
        UserRole userRole = userRoleRepository.findById(roleId).orElseThrow(() -> {
            log.error("Role not found with id: {}", roleId);
            return new DataNotFoundException("Role not found with id: " + roleId);
        });
        Set<Dashboard> dashboards = userRole.getDashboards().stream().
                filter(Dashboard::isActive).collect(Collectors.toSet());
        return dashboardMapper.dashboardsToDashboardResponseDtos(dashboards.stream().toList());
    }
}
