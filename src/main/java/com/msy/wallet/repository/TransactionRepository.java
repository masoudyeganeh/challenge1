package com.msy.wallet.repository;

import com.msy.wallet.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByTransactionDateBetween(LocalDateTime start, LocalDateTime end);
}
