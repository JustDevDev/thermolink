package org.skomi.pilot.email.service;

import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class WelcomeEmailServiceTest {

    @Mock
    private EmailService emailService;

    @Mock
    private MailConfig mailConfig;

    @InjectMocks
    private WelcomeEmailService welcomeEmailService;

    @Test
    void shouldSendEmailSuccessfully() throws MessagingException {
        // given
        String senderEmail = "no-reply@example.com";
        String userEmail = "test@example.com";
        String subject = "Welcome!";
        String welcomeMessage = "Welcome to our platform!";

        given(mailConfig.getSenderEmailAddress()).willReturn(senderEmail);

        // when
        welcomeEmailService.buildAndSendEmail(userEmail, subject, welcomeMessage);

        // then
        then(emailService).should(times(1)).sendEmail(senderEmail, userEmail, subject, welcomeMessage);
    }
}