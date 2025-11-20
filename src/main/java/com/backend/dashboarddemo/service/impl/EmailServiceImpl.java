package com.backend.dashboarddemo.service.impl;

import com.backend.dashboarddemo.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
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
        String subject = "Password reset code";
        String html = buildTemplate(
                "Password reset request",
                "You requested to reset your password. Use the code below to complete the process.",
                "Reset code",
                code,
                "This code is valid for <strong>5 minutes</strong>. If you did not request a password reset, you can safely ignore this email."
        );

        sendHtmlEmail(toEmail, subject, html);
    }

    @Async
    @Override
    public void sendInitialPassword(String toEmail, String password) {
        String subject = "Your account has been created";
        String html = buildTemplate(
                "Welcome!",
                "Your account has been successfully created.",
                "Your temporary password",
                password,
                "Please log in using this password and <strong>change it</strong> after your first login."
        );

        sendHtmlEmail(toEmail, subject, html);
    }

    @Override
    public void sendResetPassword(String toEmail, String password) {
        String subject = "Your password has been reset";
        String html = buildTemplate(
                "Password reset",
                "Your password has been reset by the administrator.",
                "Your new temporary password",
                password,
                "Please log in with this password and <strong>change it</strong> as soon as possible."
        );

        sendHtmlEmail(toEmail, subject, html);
    }


    private void sendHtmlEmail(String toEmail, String subject, String htmlContent) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            helper.setTo(toEmail);
            helper.setFrom(fromEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true -> HTML

            javaMailSender.send(mimeMessage);
            log.info("Email [{}] sent to {}", subject, toEmail);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", toEmail, e.getMessage(), e);
        }
    }

    private String buildTemplate(
            String title,
            String introText,
            String mainLabel,
            String mainValue,
            String noteText
    ) {
        return """
                <html>
                <body style="font-family: Arial, sans-serif; background-color:#f4f4f4; padding:20px;">
                  <div style="max-width:600px;margin:0 auto;background-color:#ffffff;border-radius:8px;
                              padding:24px;border:1px solid #e0e0e0;">
                    <h2 style="color:#333333; margin-top:0;">%s</h2>
                    <p style="color:#555555;font-size:14px;">%s</p>
                
                    <p style="color:#555555;font-size:14px;margin-top:18px;">%s:</p>
                    <div style="text-align:center;margin:16px 0;">
                      <code style="display:inline-block;background-color:#f5f5f5;border:1px solid #dddddd;
                                   padding:10px 18px;border-radius:4px;font-size:16px;">
                        %s
                      </code>
                    </div>
                
                    <p style="color:#777777;font-size:13px;line-height:1.5;">
                      %s
                    </p>
                
                    <hr style="border:none;border-top:1px solid #eeeeee;margin:20px 0;" />
                    <p style="color:#aaaaaa;font-size:11px;">
                      This is an automated message, please do not reply.
                    </p>
                  </div>
                </body>
                </html>
                """.formatted(
                escapeHtml(title),
                introText,
                mainLabel,
                mainValue,
                noteText
        );
    }

    private String escapeHtml(String input) {
        if (input == null) return "";
        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
