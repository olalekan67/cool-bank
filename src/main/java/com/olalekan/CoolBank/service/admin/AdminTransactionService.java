package com.olalekan.CoolBank.service.admin;

import com.olalekan.CoolBank.model.dto.admin.request.AdminTransactionAdjustmentDto;
import com.olalekan.CoolBank.model.dto.response.ApiResponse;
import org.springframework.data.domain.Pageable;

public interface AdminTransactionService {

    ApiResponse transactions(Pageable pageable);
    ApiResponse getTransaction(String reference);
    ApiResponse creditAccount(AdminTransactionAdjustmentDto adjustmentDto);
    ApiResponse debitAccount(AdminTransactionAdjustmentDto adjustmentDto);
}
