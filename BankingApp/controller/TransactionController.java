package com.aurionpro.BankingApp.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.BankingApp.dto.ApiResponse;
import com.aurionpro.BankingApp.dto.TransactionDTO;
import com.aurionpro.BankingApp.dto.TransferRequestDTO;
import com.aurionpro.BankingApp.entity.Account;
import com.aurionpro.BankingApp.exception.ResourceNotFoundException;
import com.aurionpro.BankingApp.repository.AccountRepository;
import com.aurionpro.BankingApp.repository.CustomerRepository;
//import com.aurionpro.BankingApp.service.PdfService;
import com.aurionpro.BankingApp.service.TransactionService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired private TransactionService txService;
    @Autowired private AccountRepository accountRepo;
    @Autowired private CustomerRepository customerRepo;
//    @Autowired private PdfService pdfService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public TransactionDTO create(@Valid @RequestBody TransactionDTO dto, Authentication authentication) {
    	
    	boolean isCustomer = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"));

        if (isCustomer) {
            String username = authentication.getName();

            Long loggedInCustomerId = customerRepo.findByUser_Username(username)
                    .map(c -> c.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

            dto.setCustomerId(loggedInCustomerId);
        }
    	
    	return txService.createTransaction(dto);
    }

    @GetMapping("/account/{accountId}")
    @PreAuthorize("hasRole('ADMIN') or @accountSecurity.isOwner(#accountId, authentication.name)")
    public List<TransactionDTO> byAccount(@PathVariable Long accountId) {
        return txService.getTransactionsByAccountId(accountId);
    }
    
//    @GetMapping("/account/{accountId}/passbook")
//    @PreAuthorize("hasRole('ADMIN') or @accountSecurity.isOwner(#accountId, authentication.name)")
//    public ApiResponse getPassbook(@PathVariable Long accountId) throws IOException {
//        List<TransactionDTO> txs = txService.getTransactionsByAccountId(accountId);
//
//        String email = accountRepo.findById(accountId)
//                                  .orElseThrow(() -> new ResourceNotFoundException("Account not found"))
//                                  .getCustomer()
//                                  .getEmail();
//
//        pdfService.sendTransactionPassbookPdf(email, txs);
//
//        return new ApiResponse("Email sent successfully");
//    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('ADMIN') or @accountSecurity.isOwnerByCustomerId(#customerId, authentication.name)")
    public List<TransactionDTO> byCustomer(@PathVariable Long customerId) {
        return txService.getTransactionsByCustomerId(customerId);
    }
    
    @PostMapping("/transfer")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    @Transactional
    public TransactionDTO transfer(@Valid @RequestBody TransferRequestDTO request, Authentication authentication) {
        
        boolean isCustomer = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"));

        Long loggedInCustomerId = null;
        if (isCustomer) {
            String username = authentication.getName();
            loggedInCustomerId = customerRepo.findByUser_Username(username)
                    .map(c -> c.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

            request.setCustomerId(loggedInCustomerId);
        }

        Account from = accountRepo.findById(request.getFromAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Source account not found"));

        if (isCustomer && !from.getCustomer().getCustomerId().equals(loggedInCustomerId)) {
            throw new SecurityException("You can only transfer from your own account");
        }

        Account to = accountRepo.findByAccountNumber(request.getToAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Target account not found"));

        Double amount = request.getAmount();
        if (amount <= 0) throw new IllegalArgumentException("Amount must be > 0");
        if (from.getBalance() < amount) throw new IllegalArgumentException("Insufficient funds");

        TransactionDTO debit = new TransactionDTO();
        debit.setAccountId(from.getAccountId());
        debit.setCustomerId(from.getCustomer().getCustomerId());
        debit.setAmount(amount);
        debit.setTransType("debit");
        TransactionDTO savedDebit = txService.createTransaction(debit);

        TransactionDTO credit = new TransactionDTO();
        credit.setAccountId(to.getAccountId());
        credit.setCustomerId(to.getCustomer().getCustomerId());
        credit.setAmount(amount);
        credit.setTransType("credit");
        TransactionDTO savedCredit = txService.createTransaction(credit);
        
        return savedDebit;
    }
}
