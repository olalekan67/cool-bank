package com.olalekan.CoolBank.model.dto;

import com.olalekan.CoolBank.Utils.TransactionStatus;
import com.olalekan.CoolBank.Utils.TransactionType;
import com.olalekan.CoolBank.model.Wallet;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record TransactionResponseDto(

        String senderEmail,
        String senderFullName,
        String receiverEmail,
        String receiverFullName,
        BigDecimal amount,
        TransactionStatus status,
        TransactionType type,
        String description,
        String reference,
        String externalReference,
        LocalDateTime dateTime

) {
}
