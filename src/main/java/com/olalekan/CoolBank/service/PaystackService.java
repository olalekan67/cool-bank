package com.olalekan.CoolBank.service;

import com.olalekan.CoolBank.Utils.TransactionStatus;
import com.olalekan.CoolBank.Utils.TransactionType;
import com.olalekan.CoolBank.exception.IncorrectAmountException;
import com.olalekan.CoolBank.exception.PaymentInitializationException;
import com.olalekan.CoolBank.exception.PaymentVerificationException;
import com.olalekan.CoolBank.model.AppUser;
import com.olalekan.CoolBank.model.Transaction;
import com.olalekan.CoolBank.model.Wallet;
import com.olalekan.CoolBank.model.dto.BaseResponseDto;
import com.olalekan.CoolBank.model.dto.IntialisePaymentResponse;
import com.olalekan.CoolBank.repo.AppUserRepo;
import com.olalekan.CoolBank.repo.TransactionRepo;
import com.olalekan.CoolBank.repo.WalletRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaystackService {

    private final AppUserRepo userRepo;
    private final WalletRepo walletRepo;
    private final TransactionRepo transactionRepo;
    private final RestTemplate restTemplate;

    @Value("${paystack.secret}")
    private String paystackSecret;

    @Value("${base.url}")
    private String baseUrl;

    private final String PAYSTACK_INIT_URL = "https://api.paystack.co/transaction/initialize";
    private final String PAYSTACK_VERIFY_URL = "https://api.paystack.co/transaction/verify/";

    @Transactional(noRollbackFor = PaymentInitializationException.class)
    public IntialisePaymentResponse initializePayment(BigDecimal amount){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        AppUser user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));

        Wallet wallet = user.getWallet();

        Long amountInKobo = amount.multiply(BigDecimal.valueOf(100)).longValue();

        String reference = UUID.randomUUID().toString();

        Transaction transaction = Transaction.builder()
                .type(TransactionType.FUNDING)
                .status(TransactionStatus.PENDING)
                .sourceWallet(null)
                .destinationWallet(wallet)
                .description("Paystack funding")
                .amount(amount)
                .reference(reference)
                .externalReference(null)
                .build();

        transactionRepo.save(transaction);
        try {
            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + paystackSecret);

            Map<String, Object> body = new HashMap<>();
            body.put("email", user.getEmail());
            body.put("amount", amountInKobo);
            body.put("reference", reference);
            body.put("callback_url", baseUrl + "/verifyPayment");

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(PAYSTACK_INIT_URL, entity, Map.class);

            if(response.getStatusCode() == HttpStatus.OK && response.getBody() != null){

                Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
                String authUrl = data.get("authorization_url").toString();
                return IntialisePaymentResponse.builder()
                        .authorizationUrl(authUrl)
                        .build();
            }else {
                throw new PaymentInitializationException("Paystack payment initialization failed");
            }

        } catch (Exception e) {
            transaction.setStatus(TransactionStatus.FAILED);
            transactionRepo.save(transaction);
            e.printStackTrace();
            throw new PaymentInitializationException("Error Initializing payment " + e.getMessage());
        }

    }


    @Transactional(noRollbackFor = {IncorrectAmountException.class, PaymentVerificationException.class})
    public BaseResponseDto verifyPayment(String reference){
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + paystackSecret);
            headers.set("User-Agent", "CoolBank");

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    PAYSTACK_VERIFY_URL + reference,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            if(response.getBody() == null){
                throw new PaymentVerificationException("Payment initialization failed at the provider end");
            }
            Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");

            String status = data.get("status").toString();
            Long amount = ((Number) data.get("amount")).longValue();

            String externalRef = data.get("id").toString();

            Transaction transaction = transactionRepo.findByReference(reference)
                    .orElseThrow(() -> new BadCredentialsException("This transaction does not exist"));

            BigDecimal expectedAmountInKobo = transaction.getAmount().multiply(BigDecimal.valueOf(100));
            boolean isEqualAmount = expectedAmountInKobo.compareTo(BigDecimal.valueOf(amount)) == 0;
            boolean isLessAmount = expectedAmountInKobo.compareTo(BigDecimal.valueOf(amount)) > 0;


            if(!"success".equals(status)){
                transaction.setStatus(TransactionStatus.FAILED);
                transaction.setExternalReference(externalRef);
                transactionRepo.save(transaction);
                throw new PaymentVerificationException("Payment verification failed");
            }

            if(transaction.getStatus() == TransactionStatus.SUCCESS){
                return BaseResponseDto.builder()
                        .message("Payment has already been verified")
                        .build();
            }

            if(transaction.getStatus() == TransactionStatus.FAILED){
                return BaseResponseDto.builder()
                        .message("Payment failed, please initialize new payment")
                        .build();
            }

            Wallet wallet = walletRepo.findByIdWithLock(transaction.getDestinationWallet().getId())
                    .orElseThrow(() -> new NoSuchElementException("This wallet does not exist"));
            if(!isEqualAmount){
                transaction.setStatus(TransactionStatus.FAILED);
                transaction.setExternalReference(data.get("id").toString());
                transactionRepo.save(transaction);
                throw new IncorrectAmountException(isLessAmount ?
                        "The amount send is less than the intended amount. contact our customer support for a refund" :
                        "The amount send is greater than the intended amount. contact our customer support for a refund");
            }

            transaction.setStatus(TransactionStatus.SUCCESS);
            transaction.setExternalReference(data.get("id").toString());
            wallet.setBalance(wallet.getBalance().add(transaction.getAmount()));
            walletRepo.save(wallet);
            transactionRepo.save(transaction);
            return BaseResponseDto.builder()
                    .message("Transaction completed")
                    .build();

        }catch (PaymentVerificationException e){
            throw e;
        }catch (IncorrectAmountException e){
            throw e;
        }
        catch (HttpClientErrorException | HttpServerErrorException e){
            throw new PaymentVerificationException("Paystack verification error: " + e.getMessage());
        }catch (Exception e){
            throw new PaymentVerificationException("Error verifying payment: " + e.getMessage());
        }
    }

}
