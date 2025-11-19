package com.backend.dashboarddemo.service.impl;

import com.backend.dashboarddemo.dto.request.RoleRequestDto;
import com.backend.dashboarddemo.dto.response.RoleResponseDto;
import com.backend.dashboarddemo.handler.exception.DataNotFoundException;
import com.backend.dashboarddemo.mapper.UserRoleMapper;
import com.backend.dashboarddemo.model.Dashboard;
import com.backend.dashboarddemo.model.User;
import com.backend.dashboarddemo.model.UserRole;
import com.backend.dashboarddemo.repository.DashboardRepository;
import com.backend.dashboarddemo.repository.UserRepository;
import com.backend.dashboarddemo.repository.UserRoleRepository;
import com.backend.dashboarddemo.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {
    private final UserRoleRepository userRoleRepository;
    private final DashboardRepository dashboardRepository;
    private final UserRepository userRepository;
    private final UserRoleMapper userRoleMapper;

    @Override
    public RoleResponseDto create(RoleRequestDto requestDto) {
        UserRole userRole = userRoleMapper.userRoleRequestDtoToUserRole(requestDto);

        List<Dashboard> dashboards = dashboardRepository.findAllById(requestDto.dashboardIds());
        if (dashboards.isEmpty() && !requestDto.dashboardIds().isEmpty()) {
            throw new RuntimeException("No valid dashboards found for given ids");
        }
        userRole.setDashboards(new HashSet<>(dashboards));
        UserRole saved = userRoleRepository.save(userRole);
        return userRoleMapper.userRoleToRoleResponseDto(saved);
    }

    @Override
    public List<RoleResponseDto> getAll() {
        return userRoleMapper.userRolesToRoleResponseDtos(userRoleRepository.findAll());
    }

    @Override
    public RoleResponseDto getById(Long id) {
        UserRole userRole = userRoleRepository.findById(id).orElseThrow(() -> {
            log.error("Role not found with id: {}", id);
            return new DataNotFoundException("Role not found with id: " + id);
        });
        return userRoleMapper.userRoleToRoleResponseDto(userRole);
    }

    @Override
    public void delete(Long id) {
        userRoleRepository.deleteById(id);
        log.info("Role with id {} deleted", id);
    }

    @Override
    public RoleResponseDto update(Long id, RoleRequestDto requestDto) {
        UserRole userRole = userRoleRepository.findById(id).orElseThrow(() -> {
            log.error("Role not found with id: {}", id);
            return new DataNotFoundException("Role not found with id: " + id);
        });
        userRoleMapper.updateRoleFromRequest(requestDto, userRole);
        List<Dashboard> dashboards = dashboardRepository.findAllById(requestDto.dashboardIds());
        if (dashboards.isEmpty() && !requestDto.dashboardIds().isEmpty()) {
            throw new RuntimeException("No valid dashboards found for given ids");
        }
        userRole.setDashboards(new HashSet<>(dashboards));

        UserRole saved = userRoleRepository.save(userRole);
        log.info("Role updated with id: {}", saved.getId());

        return userRoleMapper.userRoleToRoleResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasAccessToDashboard(Long dashboardId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", userId);
                    return new DataNotFoundException("User not found with id: " + userId);
                });
        return user.getRoles().stream().flatMap(
                role -> role.getDashboards().stream()
        ).anyMatch(d -> d.getId().equals(dashboardId));
    }
}
