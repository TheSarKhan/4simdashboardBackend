package com.backend.dashboarddemo.service.impl;

import com.backend.dashboarddemo.dto.request.ForgotPasswordRequestDto;
import com.backend.dashboarddemo.dto.request.LoginRequestDto;
import com.backend.dashboarddemo.dto.request.ResetPasswordRequestDto;
import com.backend.dashboarddemo.dto.request.VerifyCodeRequestDto;
import com.backend.dashboarddemo.dto.response.LoginResponseDto;
import com.backend.dashboarddemo.dto.response.MessageResponse;
import com.backend.dashboarddemo.dto.response.VerifyCodeResponseDto;
import com.backend.dashboarddemo.handler.exception.DataNotFoundException;
import com.backend.dashboarddemo.handler.exception.UserIsInActiveException;
import com.backend.dashboarddemo.handler.exception.WrongPasswordException;
import com.backend.dashboarddemo.jwt.JwtTokenProvider;
import com.backend.dashboarddemo.model.User;
import com.backend.dashboarddemo.repository.UserRepository;
import com.backend.dashboarddemo.service.AuthService;
import com.backend.dashboarddemo.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
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

    private final static String CODE_PREFIX = "pwd:code:";
    private final static String TOKEN_PREFIX = "pwd:reset token:";

    private final static Duration CODE_EXPIRATION = Duration.ofMinutes(5);
    private final static Duration TOKEN_EXPIRATION = Duration.ofMinutes(10);

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
            throw new RuntimeException("User with this email is not registered");
        }
        String code = String.format("%06d", ThreadLocalRandom.current().nextInt(0, 100000));

        String key = CODE_PREFIX + request.email();

        stringRedisTemplate.opsForValue().set(key, code, CODE_EXPIRATION);
        emailService.sendEmail(request.email(), code);

        log.info("Reset code sent to {}", request.email());
        return new MessageResponse("Reset code sent to " + request.email());
    }

    @Override
    public VerifyCodeResponseDto verifyCode(VerifyCodeRequestDto request) {
        String key = CODE_PREFIX + request.email();
        String code = stringRedisTemplate.opsForValue().get(key);
        if (code == null) {
            throw new DataNotFoundException("Code expired or not");
        }
        if (!code.equals(request.code())) {
            throw new WrongPasswordException("Wrong code");
        }

        stringRedisTemplate.delete(key);

        String resetToken = UUID.randomUUID().toString();

        String tokenKey = TOKEN_PREFIX + resetToken;

        stringRedisTemplate.opsForValue().set(tokenKey, request.email(), TOKEN_EXPIRATION);

        return new VerifyCodeResponseDto(resetToken);
    }

    @Override
    public MessageResponse resetPassword(ResetPasswordRequestDto request) {
        String resetToken = request.resetToken();
        String tokenKey = TOKEN_PREFIX + resetToken;

        String email = stringRedisTemplate.opsForValue().get(tokenKey);

        if (email == null) {
            throw new RuntimeException("Invalid or expired reset token");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);

        stringRedisTemplate.delete(tokenKey);

        return new MessageResponse("Password successfully updated");
    }
}
