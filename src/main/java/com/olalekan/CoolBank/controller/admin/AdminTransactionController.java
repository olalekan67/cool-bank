package com.olalekan.CoolBank.controller.admin;

import com.olalekan.CoolBank.model.dto.BaseResponseDto;
import com.olalekan.CoolBank.model.dto.TransactionResponseDto;
import com.olalekan.CoolBank.model.dto.admin.AdminTransactionAdjustmentDto;
import com.olalekan.CoolBank.model.dto.admin.AdminTransactionResponseBrief;
import com.olalekan.CoolBank.service.admin.AdminTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/admin/transactions")
@RequiredArgsConstructor
@Secured("ROLE_ADMIN")
public class AdminTransactionController {

    private final AdminTransactionService adminTransactionService;

   @GetMapping("/")
   public ResponseEntity<Page<AdminTransactionResponseBrief>> transactions(Pageable pageable){
       Page<AdminTransactionResponseBrief> resonses = adminTransactionService.transactions(pageable);
       return new ResponseEntity<>(resonses, HttpStatus.OK);
   }

   @GetMapping("/{reference}")
   public ResponseEntity<TransactionResponseDto> transaction(@PathVariable String reference){
        TransactionResponseDto responseDto = adminTransactionService.transaction(reference);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
   }


   @PostMapping("/credit")
   public ResponseEntity<BaseResponseDto> credit(@RequestBody AdminTransactionAdjustmentDto adjustmentDto){
       BaseResponseDto responseDto = adminTransactionService.credit(adjustmentDto);
       return new ResponseEntity<>(responseDto, HttpStatus.OK);
   }

    @PostMapping("/debit")
    public ResponseEntity<BaseResponseDto> debit(@RequestBody AdminTransactionAdjustmentDto adjustmentDto){
        BaseResponseDto responseDto = adminTransactionService.debit(adjustmentDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
