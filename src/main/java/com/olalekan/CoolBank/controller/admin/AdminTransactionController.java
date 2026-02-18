package com.olalekan.CoolBank.controller.admin;

import com.olalekan.CoolBank.model.dto.admin.request.AdminTransactionAdjustmentDto;
import com.olalekan.CoolBank.model.dto.admin.response.AdminTransactionResponseBrief;
import com.olalekan.CoolBank.model.dto.response.BaseResponseDto;
import com.olalekan.CoolBank.model.dto.response.TransactionResponseDto;
import com.olalekan.CoolBank.service.admin.AdminTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
   public ResponseEntity<Page<AdminTransactionResponseBrief>> getTransactions(Pageable pageable){
       Page<AdminTransactionResponseBrief> resonses = adminTransactionService.transactions(pageable);
       return new ResponseEntity<>(resonses, HttpStatus.OK);
   }

   @GetMapping("/{reference}")
   public ResponseEntity<TransactionResponseDto> getTransaction(@PathVariable String reference){
        TransactionResponseDto responseDto = adminTransactionService.getTransaction(reference);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
   }


   @PostMapping("/credit")
   public ResponseEntity<BaseResponseDto> creditUserAccounts(@RequestBody AdminTransactionAdjustmentDto adjustmentDto){
       BaseResponseDto responseDto = adminTransactionService.creditAccount(adjustmentDto);
       return new ResponseEntity<>(responseDto, HttpStatus.OK);
   }

    @PostMapping("/debit")
    public ResponseEntity<BaseResponseDto> debitUserAccount(@RequestBody AdminTransactionAdjustmentDto adjustmentDto){
        BaseResponseDto responseDto = adminTransactionService.debitAccount(adjustmentDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
