package org.skomi.pilot.email.listener;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skomi.pilot.email.model.ComposedEmail;
import org.skomi.pilot.email.service.EmailTemplateService;
import org.skomi.pilot.email.service.WelcomeEmailService;
import org.skomi.pilot.shared.event.SendEmailWithMessageEvent;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class SendEmailListenerTest {

    @Mock
    private WelcomeEmailService welcomeEmailService;

    @Mock
    private EmailTemplateService emailTemplateService;

    @InjectMocks
    private SendEmailListener sendEmailListener;

    @Test
    void shouldHandleSendEmailAndSendComposedEmail() {
        // given
        String userEmail = "test@example.com";
        String emailTemplate = "welcome_template";
        String[] args = {"arg1", "arg2"};
        ComposedEmail composedEmail = new ComposedEmail("Subject", "Body");
        SendEmailWithMessageEvent event = new SendEmailWithMessageEvent(this, userEmail, emailTemplate, args);

        given(emailTemplateService.composeEmail(emailTemplate, args)).willReturn(composedEmail);

        // when
        sendEmailListener.handleSendEmail(event);

        // then
        then(emailTemplateService).should(times(1)).composeEmail(emailTemplate, args);
        then(welcomeEmailService).should(times(1))
                .buildAndSendEmail(userEmail, composedEmail.subject(), composedEmail.body());
    }
}