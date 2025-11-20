package com.backend.dashboarddemo.init;

import com.backend.dashboarddemo.model.User;
import com.backend.dashboarddemo.model.UserRole;
import com.backend.dashboarddemo.repository.UserRepository;
import com.backend.dashboarddemo.repository.UserRoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminDbInitializer {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Value("${spring.admin.fullName}")
    private String fullName;

    @Value("${spring.admin.email}")
    private String email;

    @Value("${spring.admin.password}")
    private String password;

    @Value("${spring.admin.phoneNumber}")
    private String phoneNumber;

    @PostConstruct
    public void init() {
        if (userRepository.findByEmail(email).isPresent()) {
            log.info("Admin already exists with email: {}", email);
            return;
        }

        UserRole adminRole = userRoleRepository.findByName("ADMIN")
                .orElseGet(() -> {
                    UserRole role = UserRole.builder()
                            .name("ADMIN")
                            .description("System administrator")
                            .build();
                    return userRoleRepository.save(role);
                });

        User admin = User.builder()
                .fullName(fullName)
                .email(email)
                .password(passwordEncoder.encode(password))
                .phoneNumber(phoneNumber)
                .active(true)
                .roles(Set.of(adminRole))
                .build();

        userRepository.save(admin);
        log.info("Admin has been inserted with email: {}", email);
    }
}
