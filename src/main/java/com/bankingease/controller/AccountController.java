package com.bankingease.controller;

import com.bankingease.entity.Account;
import com.bankingease.entity.User;
import com.bankingease.repository.AccountRepository;
import com.bankingease.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountController(AccountRepository accountRepository,
                             UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Account> getAccounts(Authentication authentication) {
        return accountRepository.findAllByUser_Email(authentication.getName());
    }

    @GetMapping("/{id}")
    public Account getAccountById(@PathVariable Long id,
                                  Authentication authentication) {
        return accountRepository.findByIdAndUser_Email(id, authentication.getName())
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @PostMapping("/user/{userId}")
    public Account createAccount(@PathVariable Long userId,
                                 @RequestBody Account account,
                                 Authentication authentication) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.getEmail().equals(authentication.getName())) {
            throw new AccessDeniedException("Cannot create an account for another user");
        }

        account.setUser(user);

        return accountRepository.save(account);
    }

    @PutMapping("/{id}")
    public Account updateAccount(@PathVariable Long id,
                                 @RequestBody Account account,
                                 Authentication authentication) {

        Account existingAccount = accountRepository.findByIdAndUser_Email(id, authentication.getName())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        existingAccount.setAccountNumber(account.getAccountNumber());
        existingAccount.setAccountType(account.getAccountType());
        existingAccount.setBalance(account.getBalance());
        existingAccount.setStatus(account.getStatus());

        return accountRepository.save(existingAccount);
    }

    @DeleteMapping("/{id}")
    public String deleteAccount(@PathVariable Long id,
                                Authentication authentication) {

        Account account = accountRepository.findByIdAndUser_Email(id, authentication.getName())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        accountRepository.delete(account);

        return "Account deleted successfully";
    }
}