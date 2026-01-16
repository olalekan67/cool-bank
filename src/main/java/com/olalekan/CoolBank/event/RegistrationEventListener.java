package com.olalekan.CoolBank.event;


import com.olalekan.CoolBank.model.dto.SendEmailDto;
import com.olalekan.CoolBank.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationEventListener {
    private final EmailSenderService emailSenderService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleRegistrationComplete(RegistrationCompleteEvent event){
        log.info("Sending verification email to the user {}", event.user().getEmail());

        try {
            emailSenderService.sendMail(
                    SendEmailDto.builder()
                            .toEmail(event.user().getEmail())
                            .subject("Email Verification")
                            .body("The confirmation token is " + event.token() + ". Expires in 15 minutes.")
                            .build()
            );
        } catch (Exception e) {
            log.error("Failed to send verification email to {}", event.user().getEmail(), e);
        }
    }
}
