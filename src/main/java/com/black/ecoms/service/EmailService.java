package com.black.ecoms.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromEmail;

    @Value("${app.mail.subject-code}")
    private String subjectCode;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Send password reset code via email
     *
     * @param toEmail The recipient's email address
     * @param code    The generated reset code
     * @return true if email sent successfully, false otherwise
     */
    public boolean sendPasswordResetCode(String toEmail, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject(subjectCode);
            message.setText(buildPasswordResetEmailBody(code));

            mailSender.send(message);
            return true;
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            return false;
        }
    }

    /**
     * Build the email body for password reset
     *
     * @param code The generated reset code
     * @return Email body text
     */
    private String buildPasswordResetEmailBody(String code) {
        return "Hello,\n\n" +
                "You have requested to reset your password.\n\n" +
                "Your password reset code is: " + code + "\n\n" +
                "This code will expire in 10 minutes.\n\n" +
                "If you did not request a password reset, please ignore this email.\n\n" +
                "Best regards,\n" +
                "E-Commerce Team";
    }

    /**
     * Send a generic email
     *
     * @param toEmail The recipient's email address
     * @param subject The email subject
     * @param body    The email body
     * @return true if email sent successfully, false otherwise
     */
    public boolean sendEmail(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            return true;
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            return false;
        }
    }
}

