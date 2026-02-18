package br.com.fiap.file_process.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    private JavaMailSender mailSender;
    private EmailService emailService;

    @BeforeEach
    void setup() {
        mailSender = mock(JavaMailSender.class);
        emailService = new EmailService(mailSender);

        TestUtils.setField(emailService, "from", "no-reply@test.com");
    }

    @Test
    void shouldSendDefaultErrorEmail() {

        emailService.sendErrorEmail("user@test.com", "123", "erro qualquer");

        ArgumentCaptor<SimpleMailMessage> captor =
                ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(mailSender).send(captor.capture());

        SimpleMailMessage msg = captor.getValue();

        assertEquals("no-reply@test.com", msg.getFrom());
        assertEquals("user@test.com", msg.getTo()[0]);
        assertTrue(msg.getText().contains("123"));
    }

    @Test
    void shouldSendEmptyFileMessage() {

        emailService.sendErrorEmail("user@test.com", "123", "empty file");

        ArgumentCaptor<SimpleMailMessage> captor =
                ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(mailSender).send(captor.capture());

        assertTrue(captor.getValue().getText().contains("vazio")
                || captor.getValue().getText().contains("vazio")
                || captor.getValue().getText().contains("vazio"));
    }

    @Test
    void shouldHandleExceptionWhenMailSenderFails() {

        doThrow(new RuntimeException("SES down"))
                .when(mailSender)
                .send(any(SimpleMailMessage.class));

        assertDoesNotThrow(() ->
                emailService.sendErrorEmail("fail@test.com", "999", "erro qualquer")
        );

        verify(mailSender, times(1))
                .send(any(SimpleMailMessage.class));
    }

}
