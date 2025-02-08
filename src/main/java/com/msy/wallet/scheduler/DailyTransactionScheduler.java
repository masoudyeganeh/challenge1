package com.msy.wallet.scheduler;

import com.msy.wallet.model.Transaction;
import com.msy.wallet.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DailyTransactionScheduler {

    private static final Logger log = LoggerFactory.getLogger(DailyTransactionScheduler.class);

    @Autowired
    private TransactionRepository transactionRepository;

    @Scheduled(cron = "0 0 0 * * *") //runs every day at 00:00
    public void calculateDailyTransactions() {
        LocalDateTime time = LocalDateTime.now();
        Long total = transactionRepository.findTransactionsByTransactionDateIsLessThanEqual(time)
                .stream()
                .mapToLong(Transaction::getAmount)
                .sum();
        log.info("Total transactions amount: {}", total);
    }
}
