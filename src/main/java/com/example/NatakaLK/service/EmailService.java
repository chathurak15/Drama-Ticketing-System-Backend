package com.example.NatakaLK.service;

import com.example.NatakaLK.dto.responseDTO.BookingResponseDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {
    private String fromEmail ="kavindubandara2018@gmail.com";

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendWelcomeEmail(String to, String fname, String lname) {
        Context context = new Context();
        context.setVariable("name", fname + " " + lname);

        String htmlContent = templateEngine.process("welcome-email.html", context);

        sendHtmlEmail(to, "Welcome to Nataka.lk", htmlContent);
    }

    public void sendTicketEmail(String to, BookingResponseDTO responseDTO) {
        Context context = new Context();
        context.setVariable("booking", responseDTO);

        String qrBase64 = qrCodeService.generateTicketQRCode(responseDTO);
        context.setVariable("qrBase64", qrBase64);

        String htmlContent = templateEngine.process("ticket-email.html", context);
        sendHtmlEmail(to, "Your Ticket from Nataka.lk", htmlContent);
    }

    private void sendHtmlEmail(String to, String subject, String html) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setFrom(fromEmail);
            helper.setSubject(subject);
            helper.setText(html, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email to " + to, e);
        }
    }
}
