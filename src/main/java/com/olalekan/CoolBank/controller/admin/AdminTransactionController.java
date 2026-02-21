package com.olalekan.CoolBank.controller.admin;

import com.olalekan.CoolBank.model.dto.admin.request.AdminTransactionAdjustmentDto;
import com.olalekan.CoolBank.model.dto.response.ApiResponse;
import com.olalekan.CoolBank.service.admin.AdminTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/admin/transactions")
@RequiredArgsConstructor
@Secured("ROLE_ADMIN")
public class AdminTransactionController {

    private final AdminTransactionService adminTransactionService;

   @GetMapping("/")
   public ResponseEntity<ApiResponse> getTransactions(Pageable pageable){
//       Page<AdminTransactionResponseBrief> resonses = adminTransactionService.transactions(pageable);
       return ResponseEntity.status(HttpStatus.OK)
               .body(adminTransactionService.transactions(pageable));
   }

   @GetMapping("/{reference}")
   public ResponseEntity<ApiResponse> getTransaction(@PathVariable String reference){
//        TransactionResponseDto responseDto = adminTransactionService.getTransaction(reference);
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminTransactionService.getTransaction(reference));
   }


   @PostMapping("/credit")
   public ResponseEntity<ApiResponse> creditUserAccounts(@RequestBody AdminTransactionAdjustmentDto adjustmentDto){
//       BaseResponseDto responseDto = adminTransactionService.creditAccount(adjustmentDto);
       return ResponseEntity.status(HttpStatus.OK)
               .body(adminTransactionService.creditAccount(adjustmentDto));
   }

    @PostMapping("/debit")
    public ResponseEntity<ApiResponse> debitUserAccount(@RequestBody AdminTransactionAdjustmentDto adjustmentDto){
//        BaseResponseDto responseDto = adminTransactionService.debitAccount(adjustmentDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminTransactionService.debitAccount(adjustmentDto));
    }

}
