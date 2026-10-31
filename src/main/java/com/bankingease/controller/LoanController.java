package com.bankingease.controller;

import com.bankingease.entity.Loan;
import com.bankingease.entity.User;
import com.bankingease.repository.LoanRepository;
import com.bankingease.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;

    public LoanController(LoanRepository loanRepository,
                          UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    @GetMapping("/{id}")
    public Loan getLoanById(@PathVariable Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
    }

    @PostMapping("/user/{userId}")
    @Transactional
    public Loan applyForLoan(@PathVariable Long userId,
                             @RequestBody Loan loan) {

        if (loan.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Loan amount must be greater than zero");
        }

        if (loan.getTenureMonths() <= 0) {
            throw new RuntimeException("Loan tenure must be greater than zero");
        }

        if (loan.getInterestRate() < 0) {
            throw new RuntimeException("Interest rate cannot be negative");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        loan.setUser(user);
        loan.setStatus("PENDING");
        loan.setApplicationDate(LocalDateTime.now());

        return loanRepository.save(loan);
    }

    @PutMapping("/{id}/status")
    public Loan updateLoanStatus(@PathVariable Long id,
                                 @RequestParam String status) {

        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        loan.setStatus(status);

        return loanRepository.save(loan);
    }

    @DeleteMapping("/{id}")
    public String deleteLoan(@PathVariable Long id) {
        loanRepository.deleteById(id);
        return "Loan deleted successfully";
    }
}