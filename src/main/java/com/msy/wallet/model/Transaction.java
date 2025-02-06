package com.msy.wallet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "Transaction")
@Table(name = "Transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @JsonIgnore
    private Long userId;
    @JsonIgnore
    private Double amount;
    private String referenceId;
    @JsonIgnore
    private LocalDateTime transactionDate = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public Transaction setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public Transaction setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public Double getAmount() {
        return amount;
    }

    public Transaction setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public Transaction setReferenceId(String referenceId) {
        this.referenceId = referenceId;
        return this;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public Transaction setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }
}
