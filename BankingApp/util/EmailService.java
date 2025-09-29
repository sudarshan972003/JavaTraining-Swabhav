package com.aurionpro.BankingApp.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(text);
            mailSender.send(msg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
//    public void sendEmailWithAttachment(String to, String subject, String text, byte[] attachmentBytes, String attachmentName) {
//        try {
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true = multipart
//
//            helper.setTo(to);
//            helper.setSubject(subject);
//            helper.setText(text);
//            helper.addAttachment(attachmentName, new ByteArrayResource(attachmentBytes));
//
//            mailSender.send(message);
//        } catch (MessagingException ex) {
//            ex.printStackTrace();
//        }
//    }
}
