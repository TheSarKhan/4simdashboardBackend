    package com.backend.dashboarddemo.controller;

    import com.backend.dashboarddemo.dto.request.UserProfileUpdateRequestDto;
    import com.backend.dashboarddemo.dto.response.DashboardResponseDto;
    import com.backend.dashboarddemo.dto.response.UserResponseDto;
    import com.backend.dashboarddemo.service.ProfileService;
    import io.swagger.v3.oas.annotations.tags.Tag;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequiredArgsConstructor
    @RequestMapping("/api/v1/user-profile")
    @Tag(name = "User Profile", description = "User Profile API")
    public class UserProfileController {
        private final ProfileService profileService;

        @GetMapping("/dashboards")
        public ResponseEntity<List<DashboardResponseDto>> getCurrentUserDashboards() {
            return ResponseEntity.ok(profileService.getCurrentUserDashboards());
        }

        @PostMapping("/reset-password")
        public ResponseEntity<UserResponseDto> resetCurrentUserPassword() {
            UserResponseDto userResponseDto = profileService.resetCurrentUserPassword();
            return ResponseEntity.ok(userResponseDto);
        }

        @PostMapping("/update-profile")
        public ResponseEntity<UserResponseDto> updateProfile(@RequestBody UserProfileUpdateRequestDto userProfileUpdateRequestDto) {
            UserResponseDto userResponseDto = profileService.updateProfile(userProfileUpdateRequestDto);
            return ResponseEntity.ok(userResponseDto);
        }
    }
