package com.bankingease.controller;

import com.bankingease.entity.Account;
import com.bankingease.service.BankingService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/banking")
public class BankingController {

    private final BankingService bankingService;

    public BankingController(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    @PostMapping("/deposit/{accountId}")
    public Account deposit(@PathVariable Long accountId,
                           @RequestParam Double amount,
                           Authentication authentication) {

        return bankingService.deposit(accountId, amount, authentication.getName());
    }

    @PostMapping("/withdraw/{accountId}")
    public Account withdraw(@PathVariable Long accountId,
                            @RequestParam Double amount,
                            Authentication authentication) {

        return bankingService.withdraw(accountId, amount, authentication.getName());
    }
    @PostMapping("/transfer")
    public String transfer(@RequestParam Long fromAccountId,
                           @RequestParam Long toAccountId,
                           @RequestParam Double amount) {

        bankingService.transfer(fromAccountId, toAccountId, amount);

        return "Money transferred successfully";
    }
}