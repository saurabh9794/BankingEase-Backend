package com.bankingease.controller;

import com.bankingease.entity.Investment;
import com.bankingease.entity.User;
import com.bankingease.repository.InvestmentRepository;
import com.bankingease.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/investments")
public class InvestmentController {

    private final InvestmentRepository investmentRepository;
    private final UserRepository userRepository;

    public InvestmentController(InvestmentRepository investmentRepository,
                                UserRepository userRepository) {
        this.investmentRepository = investmentRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Investment> getAllInvestments() {
        return investmentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Investment getInvestmentById(@PathVariable Long id) {
        return investmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Investment not found"));
    }

    @PostMapping("/user/{userId}")
    @Transactional
    public Investment createInvestment(@PathVariable Long userId,
                                       @RequestBody Investment investment) {

        if (investment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Investment amount must be greater than zero");
        }

        if (investment.getDurationMonths() <= 0) {
            throw new RuntimeException("Investment duration must be greater than zero");
        }

        if (investment.getExpectedReturn() < 0) {
            throw new RuntimeException("Expected return cannot be negative");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        investment.setUser(user);
        investment.setStatus("ACTIVE");
        investment.setInvestmentDate(LocalDateTime.now());

        return investmentRepository.save(investment);
    }

    @PutMapping("/{id}/status")
    public Investment updateInvestmentStatus(@PathVariable Long id,
                                             @RequestParam String status) {

        Investment investment = investmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Investment not found"));

        investment.setStatus(status);

        return investmentRepository.save(investment);
    }

    @DeleteMapping("/{id}")
    public String deleteInvestment(@PathVariable Long id) {

        investmentRepository.deleteById(id);

        return "Investment deleted successfully";
    }
}