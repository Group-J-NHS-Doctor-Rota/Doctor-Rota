package edu.uob;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailTools {
    String username;
    String pwd;

    public EmailTools(String name, String password) {
        username = name;
        pwd = password;
    }

    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // Gmail:
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        // outlook:
//        mailSender.setHost("smtp-mail.outlook.com");
//        mailSender.setPort(587);

        mailSender.setUsername(username);
        mailSender.setPassword(pwd);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    public void sendSimpleMessage(String to, String subject, String text) throws MessagingException {
        JavaMailSender mailSender = getJavaMailSender();
        // use mimeMessage
        MimeMessagePreparator mailMessage = mimeMessage -> {

            MimeMessageHelper message = new MimeMessageHelper(
                    mimeMessage, true, "UTF-8");

            try {
                message.setFrom("no <no-reply@test.com>");
                message.addTo(to);
//                message.setReplyTo("no <no-reply@test.com>");
                message.setSubject(subject);
                message.setText(text);
            } catch (MessagingException e) {
                throw new MessagingException(e.toString());
            }
        };
        mailSender.send(mailMessage);

    // use host
//        String host = "xxx.xxx.8.11";  // use real ip address to replace the fake ip here
//        Properties properties = System.getProperties();
//        properties.setProperty("mail.smtp.host", host);
//        Session session = Session.getDefaultInstance(properties);
//
//        try{
//            MimeMessage message = new MimeMessage(session);
//            message.setFrom(new InternetAddress("no <no-reply@test.com>"));
//            message.addRecipient(Message.RecipientType.TO,
//                    new InternetAddress(to));
//            message.setSubject("test test");
//            message.setText("test");
//            Transport.send(message);
//        }catch (MessagingException e) {
//            throw new MessagingException(e.toString());
//        }

    }

}
