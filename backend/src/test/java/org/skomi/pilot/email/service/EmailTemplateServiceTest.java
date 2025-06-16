package org.skomi.pilot.email.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skomi.pilot.email.model.ComposedEmail;
import org.skomi.pilot.email.model.EmailTemplate;
import org.skomi.pilot.email.repository.EmailTemplateRepository;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailTemplateServiceTest {

    @Mock
    private EmailTemplateRepository emailTemplateRepository;

    @InjectMocks
    private EmailTemplateService emailTemplateService;

    @Test
    void shouldComposeEmailSuccessfullyWhenTemplateExists() {
        // given
        String templateName = "welcome";
        String[] args = {"John"};
        EmailTemplate template = new EmailTemplate();
        template.setTemplateName(templateName);
        template.setSubject("Welcome John");
        template.setBody("Hello, %s!");

        BDDMockito.given(emailTemplateRepository.findByTemplateName(templateName))
                .willReturn(Optional.of(template));

        // when
        ComposedEmail composedEmail = emailTemplateService.composeEmail(templateName, args);

        // then
        assertThat(composedEmail.subject(), is(equalTo("Welcome John")));
        assertThat(composedEmail.body(), is(equalTo("Hello, John!")));
        verify(emailTemplateRepository, times(1)).findByTemplateName(templateName);
    }

    @Test
    void shouldThrowExceptionWhenTemplateDoesNotExist() {
        // given
        String templateName = "nonexistent";
        String[] args = {"John"};
        BDDMockito.given(emailTemplateRepository.findByTemplateName(templateName))
                .willReturn(Optional.empty());

        // when
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> emailTemplateService.composeEmail(templateName, args));

        // then
        assertThat(exception.getMessage(), is(equalTo("Template not found: " + templateName)));
        verify(emailTemplateRepository, times(1)).findByTemplateName(templateName);
    }
}