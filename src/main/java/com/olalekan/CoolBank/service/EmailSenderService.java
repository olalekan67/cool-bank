package com.olalekan.CoolBank.service;

import com.olalekan.CoolBank.model.dto.SendEmailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderService {
    private final JavaMailSender mailSender;

    @Async
    public void sendMail(SendEmailDto emailDto){

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("bunyaminusalam@gmail.com");
            message.setTo(emailDto.toEmail());
            message.setSubject(emailDto.subject());
            message.setText(emailDto.body());

            mailSender.send(message);
            System.out.println("Mail sent Successfully");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Mail service failed");
        }
    }
}
