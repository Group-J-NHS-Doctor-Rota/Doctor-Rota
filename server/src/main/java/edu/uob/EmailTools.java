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
    JavaMailSender mailSender;

    public EmailTools() {
        emailFrom = ConnectionTools.getEnvOrSysVariable("EMAIL_FROM");
        pwd = ConnectionTools.getEnvOrSysVariable("EMAIL_PASSWORD");
        mailSender = getJavaMailSender();
    }

    private JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // use Gmail smtp sever:
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

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
        // use mimeMessage
        MimeMessagePreparator mailMessage = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(
                    mimeMessage, true, "UTF-8");

            try {
                message.setFrom("Doctor Rota <no-reply@doctorrota.com>");
                message.addTo(to);
                //todo maybe set this
//                message.setReplyTo("Doctor Rota <no-reply@doctorrota.com>");
                message.setSubject(subject);
                message.setText(text, true);
            } catch (MessagingException e) {
                throw new MessagingException(e.toString());
            }
        };
        mailSender.send(mailMessage);

    }

    public String passwordResetMsg(String pwd) {
        String message = "<p><h2>Reset your password</h2></p>"
                + "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Please use this temporary password to login:</p>"
                + "<p><h3>" + pwd + "</h3></p>"
                + "<p>When you login successfully, please change your password to a new one.</p>"
                + "<p>If you do not want to reset your password, contact an admin.</p>";
        return message;
    }

    public String accountCreateMsg(String username) {
        String message = "<p><h2>Create an account successfully</h2></p>"
                + "<p>Hello " + username +",</p>"
                + "<p>A new account have been created successfully on the Doctor Rota website. </p>";
        return message;
    }

    public String reminderMsg(String username) {
        String message = "<p><h2>Leave request reminder</h2></p>"
                + "<p>Hello " + username +",</p>"
                + "<p>If you haven't done already, "
                + "please submit any leave requests on the Doctor Rota website as soon as possible. </p>";
        return message;
    }

}
