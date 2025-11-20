package com.backend.dashboarddemo.service.impl;

import com.backend.dashboarddemo.dto.request.ForgotPasswordRequestDto;
import com.backend.dashboarddemo.dto.request.LoginRequestDto;
import com.backend.dashboarddemo.dto.request.ResetPasswordRequestDto;
import com.backend.dashboarddemo.dto.response.LoginResponseDto;
import com.backend.dashboarddemo.dto.response.MessageResponse;
import com.backend.dashboarddemo.handler.exception.DataNotFoundException;
import com.backend.dashboarddemo.handler.exception.UserIsInActiveException;
import com.backend.dashboarddemo.handler.exception.WrongPasswordException;
import com.backend.dashboarddemo.jwt.JwtTokenProvider;
import com.backend.dashboarddemo.model.User;
import com.backend.dashboarddemo.repository.UserRepository;
import com.backend.dashboarddemo.service.AuthService;
import com.backend.dashboarddemo.service.EmailService;
import com.backend.dashboarddemo.util.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final StringRedisTemplate stringRedisTemplate;
    private final EmailService emailService;

    private static final String CODE_PREFIX = "pwd:code:";
    private static final Duration CODE_EXPIRATION = Duration.ofMinutes(5);

    @Override
    public LoginResponseDto login(LoginRequestDto request) {
        User user = userRepository.findByEmail(request.email()).orElseThrow(() -> {
            log.error("User not found with email: {}", request.email());
            return new DataNotFoundException("User not found with email: " + request.email());
        });

        if (!user.isActive()) {
            throw new UserIsInActiveException("User is not active");
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new WrongPasswordException("Wrong password");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        return new LoginResponseDto(accessToken, refreshToken);
    }

    @Override
    public MessageResponse forgotPassword(ForgotPasswordRequestDto request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.email());

        if (optionalUser.isEmpty()) {
            throw new DataNotFoundException("User with this email is not registered");
        }

        String code = String.format("%06d",
                ThreadLocalRandom.current().nextInt(0, 1_000_000));

        String key = CODE_PREFIX + request.email();

        stringRedisTemplate
                .opsForValue()
                .set(key, code, CODE_EXPIRATION);

        emailService.sendEmail(request.email(), code);

        log.info("Reset code sent to {}", request.email());
        return new MessageResponse("Reset code sent to " + request.email());
    }

    @Override
    public MessageResponse resetPassword(ResetPasswordRequestDto request) {
        String key = CODE_PREFIX + request.email();
        String storedCode = stringRedisTemplate.opsForValue().get(key);

        if (storedCode == null) {
            throw new DataNotFoundException("Code expired or not found");
        }

        if (!storedCode.equals(request.code())) {
            throw new WrongPasswordException("Wrong code");
        }

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        String newPassword = PasswordGenerator.generateRandomPassword(8);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        try {
            emailService.sendResetPassword(user.getEmail(), newPassword);
        } catch (Exception e) {
            log.error("Failed to send reset password email to {}: {}", user.getEmail(), e.getMessage());
            throw new RuntimeException("Failed to send reset password email");
        } finally {
            stringRedisTemplate.delete(key);
        }

        return new MessageResponse("New password has been sent to your email");
    }

}
