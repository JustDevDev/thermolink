package org.skomi.pilot.email.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skomi.pilot.email.model.ComposedEmail;
import org.skomi.pilot.email.service.EmailTemplateService;
import org.skomi.pilot.email.service.WelcomeEmailService;
import org.skomi.pilot.shared.event.SendEmailWithMessageEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SendEmailListener {

    private final WelcomeEmailService welcomeEmailService;
    private final EmailTemplateService emailTemplateService;

    /**
     * Handles the {@link SendEmailWithMessageEvent} to compose and send an email
     * using the provided template and arguments.
     *
     * @param event the event containing the user's email address, the template name,
     *              and arguments to be used for generating the email content
     */
    @EventListener
    public void handleSendEmail(SendEmailWithMessageEvent event) {
        ComposedEmail composedEmail = emailTemplateService.composeEmail(event.getEmailTemplate(), event.getArgs());
        welcomeEmailService.buildAndSendEmail(event.getUserEmail(), composedEmail.subject(), composedEmail.body());
    }
}
