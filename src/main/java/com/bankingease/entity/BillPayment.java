package com.bankingease.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "bill_payments")
public class BillPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String billType;

    private String billNumber;

    private BigDecimal amount;;

    private String status;

    private LocalDateTime paymentDate;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public BillPayment() {
    }

    public BillPayment(String billType,
                       String billNumber,
                       BigDecimal amount,
                       String status,
                       LocalDateTime paymentDate,
                       Account account){
        this.billType = billType;
        this.billNumber = billNumber;
        this.amount = amount;
        this.status = status;
        this.paymentDate = paymentDate;
        this.account = account;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}