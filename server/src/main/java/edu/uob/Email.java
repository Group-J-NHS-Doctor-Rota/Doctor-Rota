package edu.uob;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

import static org.junit.Assert.fail;

public class Email {
    String username = "";
    String pwd = "";

    public Email(String name, String password) {
        username = name;
        pwd = password;
    }

//    https://www.baeldung.com/spring-email
//    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername(username);
        mailSender.setPassword(pwd);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        JavaMailSender sender = getJavaMailSender();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@emailtest.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        try {
            sender.send(message);
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

}
