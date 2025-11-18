package com.backend.dashboarddemo.controller;

import com.backend.dashboarddemo.dto.request.DashboardRequestDto;
import com.backend.dashboarddemo.dto.request.UpdatedDashboardRequestDto;
import com.backend.dashboarddemo.dto.response.DashboardResponseDto;
import com.backend.dashboarddemo.service.DashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dashboards")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Dashboard API")
@PreAuthorize("hasRole('ADMIN')")
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<List<DashboardResponseDto>> getAll() {
        return ResponseEntity.ok(dashboardService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DashboardResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(dashboardService.getById(id));
    }

    @PostMapping
    public ResponseEntity<DashboardResponseDto> add(@RequestBody DashboardRequestDto dashboardRequestDto) {
        return ResponseEntity.ok(dashboardService.add(dashboardRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DashboardResponseDto> update(@PathVariable Long id, @RequestBody UpdatedDashboardRequestDto updatedDashboardRequestDto) {
        return ResponseEntity.ok(dashboardService.update(id, updatedDashboardRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        dashboardService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
