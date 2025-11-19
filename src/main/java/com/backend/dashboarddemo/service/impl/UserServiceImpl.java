package com.backend.dashboarddemo.service.impl;

import com.backend.dashboarddemo.dto.request.UserRequestDto;
import com.backend.dashboarddemo.dto.request.UserResetPasswordRequestDto;
import com.backend.dashboarddemo.dto.response.UserResponseDto;
import com.backend.dashboarddemo.handler.exception.DataNotFoundException;
import com.backend.dashboarddemo.handler.exception.UserAlreadyExistException;
import com.backend.dashboarddemo.mapper.UserMapper;
import com.backend.dashboarddemo.model.User;
import com.backend.dashboarddemo.model.UserRole;
import com.backend.dashboarddemo.repository.UserRepository;
import com.backend.dashboarddemo.repository.UserRoleRepository;
import com.backend.dashboarddemo.service.EmailService;
import com.backend.dashboarddemo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final StringRedisTemplate redisTemplate;
    private final UserMapper userMapper;

    private static final String PASSWORD_ALPHABET =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";

    private static final int DEFAULT_PASSWORD_LENGTH = 8;

    private static final String INIT_PWD_PREFIX = "init_pwd:user:";

    private static final Duration INIT_PWD_TTL = Duration.ofMinutes(15);


    @Override
    public UserResponseDto getById(Long id) {
        return userMapper.userToUserResponseDto(userRepository.findById(id).orElseThrow(() -> {
            log.error("User not found with id: {}", id);
            return new DataNotFoundException("User not found with id: " + id);
        }));
    }

    @Override
    public List<UserResponseDto> getAll() {
        return userMapper.usersToUserResponseDtos(userRepository.findAll());
    }

    @Override
    @Transactional
    public UserResponseDto create(UserRequestDto userRequestDto) {
        if (userRepository.findByEmail(userRequestDto.email()).isPresent()) {
            throw new UserAlreadyExistException("User with this email already exists");
        }
        User user = User.builder()
                .fullName(userRequestDto.fullName())
                .email(userRequestDto.email())
                .phoneNumber(userRequestDto.phoneNumber())
                .createdAt(Instant.now())
                .active(false)
                .build();
        if (userRequestDto.roleIds() == null || userRequestDto.roleIds().isEmpty()) {
            throw new RuntimeException("At least one role must be selected");
        }
        List<UserRole> userRoles = userRoleRepository.findAllById(userRequestDto.roleIds());
        user.setRoles(Set.copyOf(userRoles));

        String password = generateRandomPassword(DEFAULT_PASSWORD_LENGTH);

        user.setPassword(passwordEncoder.encode(password));
        user.setActive(true);
        User saved = userRepository.save(user);

        String key = INIT_PWD_PREFIX + saved.getId();
        redisTemplate.opsForValue().set(key, password, INIT_PWD_TTL);
        emailService.sendInitialPassword(saved.getEmail(), password);
        return userMapper.userToUserResponseDto(saved);
    }

    @Override
    @Transactional
    public UserResponseDto resetPassword(UserResetPasswordRequestDto userResetPasswordRequestDto) {
        User user = userRepository.findById(userResetPasswordRequestDto.userId()).orElseThrow(() -> {
            log.error("User not found with id: {}", userResetPasswordRequestDto.userId());
            return new DataNotFoundException("User not found with id: " + userResetPasswordRequestDto.userId());
        });

        String password = generateRandomPassword(DEFAULT_PASSWORD_LENGTH);
        user.setPassword(passwordEncoder.encode(password));
        User saved = userRepository.save(user);

        String resetPasswordKey = INIT_PWD_PREFIX + saved.getId();
        redisTemplate.opsForValue().set(resetPasswordKey, password, INIT_PWD_TTL);
        emailService.sendResetPassword(saved.getEmail(), password);
        return userMapper.userToUserResponseDto(saved);
    }

    @Override
    @Transactional
    public UserResponseDto update(Long id, UserRequestDto userRequestDto) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            log.error("User not found with id: {}", id);
            return new DataNotFoundException("User not found with id: " + id);
        });
        userMapper.updateUserFromRequest(userRequestDto, user);
        if (userRequestDto.roleIds() == null || userRequestDto.roleIds().isEmpty()) {
            throw new RuntimeException("At least one role must be selected");
        }
        List<UserRole> userRoles = userRoleRepository.findAllById(userRequestDto.roleIds());
        user.setRoles(Set.copyOf(userRoles));

        User saved = userRepository.save(user);
        return userMapper.userToUserResponseDto(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
        log.info("User with id {} deleted", id);
    }

    private String generateRandomPassword(int length) {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder(length);
        int alphabetLength = PASSWORD_ALPHABET.length();

        for (int i = 0; i < length; i++) {
            int nexted = secureRandom.nextInt(alphabetLength);
            stringBuilder.append(PASSWORD_ALPHABET.charAt(nexted));
        }
        return stringBuilder.toString();
    }
}
