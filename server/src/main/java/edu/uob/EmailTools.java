package edu.uob;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.MessagingException;
import java.util.Properties;

public class EmailTools {
    String emailFrom;
    String pwd;

    public EmailTools() {
        emailFrom = ConnectionTools.getEnvOrSysVariable("EMAIL_FROM");
        pwd = ConnectionTools.getEnvOrSysVariable("EMAIL_PASSWORD");
    }

    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // Gmail:
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        // outlook:
//        mailSender.setHost("smtp-mail.outlook.com");
//        mailSender.setPort(587);

        mailSender.setUsername(emailFrom);
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

    }

    public String passwordResetMsg(String pwd) {
        //todo modify the message content
        //todo invalid html format
        String message = "<p><h2>Reset your password</h2></p>"
                + "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Please use this temporary password to login:</p>"
                + "<p><h3>" + pwd + "</h3></p>"
                + "<p>When you login successfully, please change your password to your own one.</p>"
                + "<p>If you do not want to reset your password, ignore this email.</p>";
        return message;
    }

    public String accountCreateMsg(String username) {
        //todo modify the message content
        // todo invalid html format
        String message = "<p><h2>Create account successfully</h2></p>"
                + "<p>Hello " + username +",</p>"
                + "<p>You have created an account on XXX website.</p>";
        return message;
    }

}
