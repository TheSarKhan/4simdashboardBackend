package com.backend.dashboarddemo.service;

public interface EmailService {
    void sendEmail(String toEmail, String code);

    void sendInitialPassword(String toEmail, String password);

    void sendResetPassword(String toEmail, String password);
}
