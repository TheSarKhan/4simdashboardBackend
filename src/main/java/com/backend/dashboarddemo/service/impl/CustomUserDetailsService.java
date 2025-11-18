package com.backend.dashboarddemo.service.impl;

import com.backend.dashboarddemo.handler.exception.EmailCanNotFoundException;
import com.backend.dashboarddemo.model.User;
import com.backend.dashboarddemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            log.error("User not found with email: {}", email);
            return new EmailCanNotFoundException("User not found with email:" + email);
        });
        return user.customUserDetails();
    }
}
