package org.skomi.pilot.email.service;

import lombok.AllArgsConstructor;
import org.skomi.pilot.email.model.ComposedEmail;
import org.skomi.pilot.email.model.EmailTemplate;
import org.skomi.pilot.email.repository.EmailTemplateRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailTemplateService {

    private final EmailTemplateRepository emailTemplateRepository;

    /**
     * Retrieves the email template by its name and applies given arguments to construct the final email.
     *
     * @param templateName the unique template name
     * @param args         arguments to be formatted into the template's placeholders
     * @return a composed email with subject and body
     */
    public ComposedEmail composeEmail(String templateName, String[] args) {
        EmailTemplate template = emailTemplateRepository.findByTemplateName(templateName)
                .orElseThrow(() -> new RuntimeException("Template not found: " + templateName));
        String subject = template.getSubject();
        String body = String.format(template.getBody(), (Object[]) args);
        return new ComposedEmail(subject, body);
    }
}