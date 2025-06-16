package org.skomi.pilot.email.repository;

import org.skomi.pilot.email.model.EmailTemplate;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EmailTemplateRepository extends CrudRepository<EmailTemplate, Long> {
    /**
     * Finds and retrieves an email template by its unique template name.
     *
     * @param templateName the unique name of the email template to be retrieved
     * @return an optional containing the email template if found, or an empty optional if not found
     */
    Optional<EmailTemplate> findByTemplateName(String templateName);
}