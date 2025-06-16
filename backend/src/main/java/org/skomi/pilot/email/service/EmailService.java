package org.skomi.pilot.email.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final MailConfig mailConfig;

    /**
     * Sends an email using the specified parameters. This method constructs the email payload
     * and communicates with the ZeptoMail API to send the email.
     *
     * @param from     the email address of the sender
     * @param to       the email address of the recipient
     * @param subject  the subject of the email
     * @param bodyHtml the HTML content of the email body
     */
    public void sendEmail(String from, String to, String subject, String bodyHtml) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("Authorization", mailConfig.getZeptoApiKey());

        Map<String, Object> payload = new HashMap<>();
        payload.put("from", Map.of("address", from));
        payload.put("to", List.of(Map.of("email_address", Map.of("address", to))));
        payload.put("subject", subject);
        payload.put("htmlbody", bodyHtml);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(mailConfig.getZeptoApiUrl(), request, String.class);
            log.info("Email sent successfully: HTTP {}", response.getStatusCode());
        } catch (HttpStatusCodeException ex) {
            log.error("ZeptoMail API returned error: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString());
        } catch (RestClientException ex) {
            log.error("Error sending email via ZeptoMail: {}", ex.getMessage(), ex);
        }
    }
}