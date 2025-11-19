package com.backend.dashboarddemo.controller;

import com.backend.dashboarddemo.dto.request.UserRequestDto;
import com.backend.dashboarddemo.dto.request.UserResetPasswordRequestDto;
import com.backend.dashboarddemo.dto.response.UserResponseDto;
import com.backend.dashboarddemo.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "User", description = "User API")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    private final UserService userService;


    @PostMapping("/create")
    public ResponseEntity<UserResponseDto> create(@RequestBody UserRequestDto dto) {
        return ResponseEntity.ok(userService.create(dto));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<UserResponseDto> resetPassword(
            @RequestBody UserResetPasswordRequestDto dto
    ) {
        return ResponseEntity.ok(userService.resetPassword(dto));
    }

    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(
            @PathVariable Long id,
            @RequestBody UserRequestDto requestDto) {
        return ResponseEntity.ok(userService.update(id, requestDto));
    }
}
