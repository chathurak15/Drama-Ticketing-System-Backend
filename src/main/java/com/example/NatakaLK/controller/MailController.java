package com.example.NatakaLK.controller;

import com.example.NatakaLK.dto.requestDTO.MailDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/mail")
@CrossOrigin
public class MailController {
    @Autowired
    private JavaMailSender mailSender;

    @PostMapping ("/send")
    public String sendMail(@RequestBody MailDetailDTO mailDetailDTO) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(mailDetailDTO.getTo());
            message.setSubject(mailDetailDTO.getSubject());
            message.setFrom(mailDetailDTO.getFrom());
            message.setText(mailDetailDTO.getMessage());
            mailSender.send(message);
            return "Email sent";
        }catch (Exception e){
            return " "+ e ;
        }

    }
}
