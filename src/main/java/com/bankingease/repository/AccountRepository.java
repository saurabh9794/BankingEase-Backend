package com.bankingease.repository;

import com.bankingease.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
	List<Account> findAllByUser_Email(String email);

	Optional<Account> findByIdAndUser_Email(Long id, String email);
}