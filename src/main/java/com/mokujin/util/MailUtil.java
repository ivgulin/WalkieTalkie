package com.mokujin.util;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MailUtil {

    public static boolean isAddressValid(String to) {
        String header = "Email validation";
        String text = "Hello, this message was send because of registration in Walkie Talkie messenger.";
        return sendMail(to, header, text);
    }

    public static boolean sendAfterRegistrationMail(String to) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        String header = "Congratulations, " + username + "!";
        String text = "You have successfully signed up. your password:" + password;
        return sendMail(to, header, text);
    }

    private static boolean sendMail(String to, String header, String text) {
        Properties senderProperties = createSenderProperties();
        String from = senderProperties.getProperty("sender.email");
        String password = senderProperties.getProperty("sender.password");

        Properties props = System.getProperties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.store.protocol", "pop3");
        props.put("mail.transport.protocol", "smtp");
        Session session = Session.getDefaultInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, password);
                    }
                });
        try {

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(header);
            message.setText(text);

            Transport.send(message);
            System.out.println("message sent successfully....");

        } catch (MessagingException mex) {
            mex.printStackTrace();
            return false;
        }
        return true;
    }

    private static Properties  createSenderProperties() {
        Properties properties = new Properties();
        InputStream propertiesStream = Object.class.getResourceAsStream("config.properties");
        if (propertiesStream != null) {
            try {
                properties.load(propertiesStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return properties;
    }
}
