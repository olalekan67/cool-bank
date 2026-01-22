package com.olalekan.CoolBank.model.dto.admin;

import com.olalekan.CoolBank.Utils.TransactionStatus;
import com.olalekan.CoolBank.Utils.TransactionType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Builder

public record AdminTransactionResponseBrief(
        String senderEmail,
        String senderFullName,
        String receiverEmail,
        String receiverFullName,
        BigDecimal amount,
        String reference,
        String externalReference,
        LocalDateTime dateTime
) {
}
