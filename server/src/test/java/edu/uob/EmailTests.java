package edu.uob;

import org.junit.jupiter.api.Test;

import javax.mail.MessagingException;

public class EmailTests {

    @Test
    void testEmailTools() throws MessagingException {
        String from = ConnectionTools.getEnvOrSysVariable("EMAIL_FROM");
        String emailPassword = ConnectionTools.getEnvOrSysVariable("EMAIL_PASSWORD");
        String to = ConnectionTools.getEnvOrSysVariable("EMAIL_TO");
        // Gmail:
        EmailTools email = new EmailTools(from, emailPassword);
        email.sendSimpleMessage(to, "email function test", "test test");
    }
}
