package com.codecool.sv_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Value("${spring.mail.username}")
    private String email;

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(email);
        mailSender.send(message);
    }

    public void sendActivationTokenEmail(String token, String email, long id) {

        String activationLink = "http://localhost:5173/api/auth/activate" +
                "?token=" +
                token + "&userId=" + id;

        sendEmail(email, "Activate your account",
                "Click here to activate your account: " + activationLink);
    }
}
