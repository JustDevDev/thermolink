package org.skomi.pilot.shared.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SendEmailWithMessageEvent extends ApplicationEvent {

    String userEmail;
    String emailTemplate;
    String[] args;

    public SendEmailWithMessageEvent(Object source, String userEmail, String emailTemplate, String[] args) {
        super(source);
        this.userEmail = userEmail;
        this.emailTemplate = emailTemplate;
        this.args = args;
    }
}
