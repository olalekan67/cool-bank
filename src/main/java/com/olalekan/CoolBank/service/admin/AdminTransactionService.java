package com.olalekan.CoolBank.service.admin;

import com.olalekan.CoolBank.model.dto.admin.request.AdminTransactionAdjustmentDto;
import com.olalekan.CoolBank.model.dto.admin.response.AdminTransactionResponseBrief;
import com.olalekan.CoolBank.model.dto.response.BaseResponseDto;
import com.olalekan.CoolBank.model.dto.response.TransactionResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminTransactionService {

    Page<AdminTransactionResponseBrief> transactions(Pageable pageable);
    TransactionResponseDto getTransaction(String reference);
    BaseResponseDto creditAccount(AdminTransactionAdjustmentDto adjustmentDto);
    BaseResponseDto debitAccount(AdminTransactionAdjustmentDto adjustmentDto);
}
