package com.backend.dashboarddemo.service.impl;

import com.backend.dashboarddemo.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String toEmail, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
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
}

