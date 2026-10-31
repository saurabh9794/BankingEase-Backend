package com.bankingease.service;

import com.bankingease.entity.Account;
import com.bankingease.entity.Transaction;
import com.bankingease.repository.AccountRepository;
import com.bankingease.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class BankingService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public BankingService(AccountRepository accountRepository,
                          TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
        public Account deposit(Long accountId, Double amount, String userEmail) {

        if (amount <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }

        Account account = accountRepository.findByIdAndUser_Email(accountId, userEmail)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        BigDecimal depositAmount = BigDecimal.valueOf(amount);

        BigDecimal newBalance =
                account.getBalance().add(depositAmount);

        account.setBalance(newBalance);

        accountRepository.save(account);

        Transaction transaction = new Transaction(
                "DEPOSIT",
                BigDecimal.valueOf(amount),
                "Money deposited",
                LocalDateTime.now(),
                account
        );

        transactionRepository.save(transaction);

        return account;
    }

    @Transactional
        public Account withdraw(Long accountId, Double amount, String userEmail) {

        if (amount <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }

        Account account = accountRepository.findByIdAndUser_Email(accountId, userEmail)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        BigDecimal withdrawAmount = BigDecimal.valueOf(amount);

        if (account.getBalance().compareTo(withdrawAmount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        BigDecimal newBalance =
                account.getBalance().subtract(withdrawAmount);

        account.setBalance(newBalance);

        accountRepository.save(account);

        Transaction transaction = new Transaction(
                "WITHDRAW",
                BigDecimal.valueOf(amount),
                "Money withdrawn",
                LocalDateTime.now(),
                account
        );

        transactionRepository.save(transaction);

        return account;
    }

    @Transactional
    public void transfer(Long fromAccountId,
                         Long toAccountId,
                         Double amount) {

        if (amount <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }

        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Sender account not found"));

        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("Receiver account not found"));

        if (fromAccountId.equals(toAccountId)) {
            throw new RuntimeException("Cannot transfer money to the same account");
        }

        BigDecimal transferAmount = BigDecimal.valueOf(amount);

        if (fromAccount.getBalance().compareTo(transferAmount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        fromAccount.setBalance(
                fromAccount.getBalance().subtract(transferAmount)
        );

        toAccount.setBalance(
                toAccount.getBalance().add(transferAmount)
        );

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction senderTransaction = new Transaction(
                "TRANSFER",
                BigDecimal.valueOf(amount),
                "Money transferred to account "
                        + toAccount.getAccountNumber(),
                LocalDateTime.now(),
                fromAccount
        );

        Transaction receiverTransaction = new Transaction(
                "TRANSFER",
                BigDecimal.valueOf(amount),
                "Money received from account "
                        + fromAccount.getAccountNumber(),
                LocalDateTime.now(),
                toAccount
        );

        transactionRepository.save(senderTransaction);
        transactionRepository.save(receiverTransaction);
    }
}