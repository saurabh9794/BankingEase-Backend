package com.bankingease.repository;

import com.bankingease.entity.BillPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillPaymentRepository extends JpaRepository<BillPayment, Long> {
}