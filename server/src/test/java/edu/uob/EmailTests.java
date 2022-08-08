package edu.uob;

import org.junit.jupiter.api.Test;

import javax.mail.MessagingException;

public class EmailTests {

    @Test
    void testEmailTools() throws MessagingException {
        //todo delete
//        String from = ConnectionTools.getEnvOrSysVariable("EMAIL_FROM");
//        String emailPassword = ConnectionTools.getEnvOrSysVariable("EMAIL_PASSWORD");
        String to = ConnectionTools.getEnvOrSysVariable("EMAIL_TO");
        // Gmail:
        EmailTools email = new EmailTools();
        email.sendSimpleMessage(to, "email function test", "test test");
    }
}
