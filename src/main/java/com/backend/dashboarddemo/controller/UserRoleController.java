package com.backend.dashboarddemo.controller;

import com.backend.dashboarddemo.dto.request.RoleRequestDto;
import com.backend.dashboarddemo.dto.response.RoleResponseDto;
import com.backend.dashboarddemo.service.UserRoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-roles")
@Tag(name = "User Role", description = "User Role API")
@PreAuthorize("hasRole('ADMIN')")
public class UserRoleController {
    private final UserRoleService userRoleService;

    @PostMapping
    public ResponseEntity<RoleResponseDto> create(@RequestBody RoleRequestDto requestDto) {
        return ResponseEntity.ok(userRoleService.create(requestDto));
    }

    @GetMapping()
    public ResponseEntity<List<RoleResponseDto>> getAll() {
        return ResponseEntity.ok(userRoleService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userRoleService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userRoleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleResponseDto> update(
            @PathVariable Long id,
            @RequestBody RoleRequestDto requestDto) {
        return ResponseEntity.ok(userRoleService.update(id, requestDto));
    }

    @GetMapping("/check-role")
    public ResponseEntity<Boolean> hasAccessToDashboard(@RequestParam Long dashboardId, @RequestParam Long userId) {
        return ResponseEntity.ok(userRoleService.hasAccessToDashboard(dashboardId, userId));
    }
}
