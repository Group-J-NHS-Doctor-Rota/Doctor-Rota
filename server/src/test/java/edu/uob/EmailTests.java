package edu.uob;

import org.junit.jupiter.api.Test;

import javax.mail.MessagingException;

public class EmailTests {

    @Test
    void testEmailTools() throws MessagingException {
        String to = ConnectionTools.getEnvOrSysVariable("EMAIL_TO");
        EmailTools email = new EmailTools();
        email.sendSimpleMessage(to, "email function test", "test test");
    }
}
