package com.bankingease.controller;

import com.bankingease.entity.Account;
import com.bankingease.entity.BillPayment;
import com.bankingease.repository.AccountRepository;
import com.bankingease.repository.BillPaymentRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/bills")
public class BillPaymentController {

    private final BillPaymentRepository billPaymentRepository;
    private final AccountRepository accountRepository;

    public BillPaymentController(BillPaymentRepository billPaymentRepository,
                                 AccountRepository accountRepository) {
        this.billPaymentRepository = billPaymentRepository;
        this.accountRepository = accountRepository;
    }

    @GetMapping
    public List<BillPayment> getAllBills() {
        return billPaymentRepository.findAll();
    }

    @GetMapping("/{id}")
    public BillPayment getBillById(@PathVariable Long id) {
        return billPaymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill payment not found"));
    }

    @PostMapping("/account/{accountId}")
    @Transactional
    public BillPayment payBill(@PathVariable Long accountId,
                               @RequestBody BillPayment billPayment) {

        // Validate bill amount
        if (billPayment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Bill amount must be greater than zero");
        }

        // Find account
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Check balance
        BigDecimal billAmount = billPayment.getAmount();

        if (account.getBalance().compareTo(billAmount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(
                account.getBalance().subtract(billAmount)
        );
        accountRepository.save(account);

        // Set bill details
        billPayment.setAccount(account);
        billPayment.setStatus("SUCCESS");
        billPayment.setPaymentDate(LocalDateTime.now());

        // Save bill payment
        return billPaymentRepository.save(billPayment);
    }

    @DeleteMapping("/{id}")
    public String deleteBill(@PathVariable Long id) {

        billPaymentRepository.deleteById(id);

        return "Bill payment deleted successfully";
    }
}