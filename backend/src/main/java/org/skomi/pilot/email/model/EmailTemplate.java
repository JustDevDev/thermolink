package org.skomi.pilot.email.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.UUID;

/**
 * Used for retrieving email templates
 */
@Data
public class EmailTemplate {

    @Id
    private UUID id;

    private String templateName;
    private String subject;
    private String body;

}
