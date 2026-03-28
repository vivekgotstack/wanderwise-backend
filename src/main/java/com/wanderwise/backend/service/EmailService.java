package com.wanderwise.backend.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendNewsletter(String to) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("✈️ WanderWise Flight Updates");
        msg.setText("New flights are now available! Check out the latest deals 🚀");

        mailSender.send(msg);
    }
}