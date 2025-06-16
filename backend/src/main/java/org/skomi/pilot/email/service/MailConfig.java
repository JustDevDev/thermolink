package org.skomi.pilot.email.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class MailConfig {

    @Value("${spring.mail.sender.address:noreply@just-dev.dev}")
    private String senderEmailAddress;

    @Value("${spring.zeptomail.api.key:undefined}")
    private String zeptoApiKey;

    @Value("${spring.zeptomail.api.url:https://api.zeptomail.eu/v1.1/email}")
    private String zeptoApiUrl;
}