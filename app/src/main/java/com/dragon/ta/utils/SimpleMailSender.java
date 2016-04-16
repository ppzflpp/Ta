package com.dragon.ta.utils;

import com.sun.mail.util.MailSSLSocketFactory;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SimpleMailSender {

    public boolean sendTextMail(MailSenderInfo mailInfo) {

        MyAuthenticator authenticator = null;
        Properties props = mailInfo.getProperties();
        if (mailInfo.isValidate()) {
            authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
        }

        Session sendMailSession = Session.getDefaultInstance(props, authenticator);
        try {

            Message mailMessage = new MimeMessage(sendMailSession);

            Address from = new InternetAddress(mailInfo.getFromAddress());

            mailMessage.setFrom(from);

            Address to = new InternetAddress(mailInfo.getToAddress());
            mailMessage.setRecipient(Message.RecipientType.TO, to);

            mailMessage.setSubject(mailInfo.getSubject());

            mailMessage.setSentDate(new Date());

            String mailContent = mailInfo.getContent();
            mailMessage.setText(mailContent);

            Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        return false;
    }

}
