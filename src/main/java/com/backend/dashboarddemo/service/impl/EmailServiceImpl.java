package com.backend.dashboarddemo.service.impl;

import com.backend.dashboarddemo.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendEmail(String toEmail, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setFrom(fromEmail);
            message.setSubject("Password reset code");
            message.setText("""
                    Your password reset code is: %s
                    
                    This code is valid for 5 minutes.
                    """.formatted(code));

            javaMailSender.send(message);
            log.info("Reset code email sent to {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send reset code email to {}: {}", toEmail, e.getMessage());
        }
    }

    @Override
    public void sendInitialPassword(String toEmail, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setFrom(fromEmail);
        message.setSubject("Your account has been created");
        message.setText("""
                Your account has been created.
                
                Your temporary password is:
                %s
                
                Please log in and change your password after first login.
                """.formatted(password));

        javaMailSender.send(message);
    }

    @Override
    public void sendResetPassword(String toEmail, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setFrom(fromEmail);
        message.setSubject("Your password has been reset");
        message.setText("""
                Your password has been reset by administrator.
                
                Your new temporary password is:
                %s
                
                Please log in and change your password after first login.
                """.formatted(password));

        javaMailSender.send(message);
    }
}

