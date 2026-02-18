package com.olalekan.CoolBank.service;

import com.olalekan.CoolBank.model.dto.SendEmailDto;
import org.springframework.stereotype.Service;

public interface EmailSenderService {
    void sendMail(SendEmailDto emailDto);
}
