package com.bankingease.controller;

import com.bankingease.entity.Beneficiary;
import com.bankingease.entity.User;
import com.bankingease.repository.BeneficiaryRepository;
import com.bankingease.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beneficiaries")
public class BeneficiaryController {

    private final BeneficiaryRepository beneficiaryRepository;
    private final UserRepository userRepository;

    public BeneficiaryController(BeneficiaryRepository beneficiaryRepository,
                                 UserRepository userRepository) {
        this.beneficiaryRepository = beneficiaryRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Beneficiary> getAllBeneficiaries() {
        return beneficiaryRepository.findAll();
    }

    @GetMapping("/{id}")
    public Beneficiary getBeneficiaryById(@PathVariable Long id) {
        return beneficiaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Beneficiary not found"));
    }

    @PostMapping("/user/{userId}")
    public Beneficiary createBeneficiary(@PathVariable Long userId,
                                         @RequestBody Beneficiary beneficiary) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        beneficiary.setUser(user);

        return beneficiaryRepository.save(beneficiary);
    }

    @DeleteMapping("/{id}")
    public String deleteBeneficiary(@PathVariable Long id) {

        beneficiaryRepository.deleteById(id);

        return "Beneficiary deleted successfully";
    }
}