package org.skomi.pilot.shared.publisher;

import lombok.RequiredArgsConstructor;
import org.skomi.pilot.shared.event.SendEmailWithMessageEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * Calls to send email
     */
    public void sendEmail(String userEmail, String emailTemplateName, String ... args) {
        applicationEventPublisher.publishEvent(
                new SendEmailWithMessageEvent(this,
                        userEmail,
                        emailTemplateName,
                        args
                )
        );
    }
}
