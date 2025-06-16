package org.skomi.pilot.email.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WelcomeEmailService {

    private final EmailService emailService;
    private final MailConfig mailConfig;

    /**
     * Sends a welcome email to a specified user with a given subject and message.
     *
     * @param userEmail      the email address of the recipient
     * @param subject        the subject of the email
     * @param welcomeMessage the message content of the welcome email
     */
    public void buildAndSendEmail(String userEmail, String subject, String welcomeMessage) {
        emailService.sendEmail(mailConfig.getSenderEmailAddress(), userEmail, subject, welcomeMessage);
        log.info("[EMAIL] Sent email to {}. Subject: {}.", userEmail, subject);
    }
}