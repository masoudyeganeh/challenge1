package com.msy.wallet.service;

import com.msy.wallet.exception.ErrorCode;
import com.msy.wallet.exception.WalletServiceException;
import com.msy.wallet.model.Transaction;
import com.msy.wallet.model.User;
import com.msy.wallet.repository.TransactionRepository;
import com.msy.wallet.repository.UserRepository;
import com.msy.wallet.scheduler.DailyTransactionScheduler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class WalletServiceImpl implements WalletService {

    private static final Logger log = LoggerFactory.getLogger(DailyTransactionScheduler.class);
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public WalletServiceImpl(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public User getBalance(Long userId) throws WalletServiceException {
        log.debug("Entering getBalance for userId: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found for userId: {}", userId);
                    return new WalletServiceException(ErrorCode.USER_NOT_FOUND, userId);
                });
        log.info("Retrieved balance for userId: {}, balance: {}", userId, user.getBalance());
        return user;
    }

    @Override
    @Transactional
    public Transaction addMoney(Long userId, String amount) {
        log.debug("Entering addMoney for userId: {} with amount: {}", userId, amount);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found for userId: {}", userId);
                    return new WalletServiceException(ErrorCode.USER_NOT_FOUND, userId);
                });

        log.info("Current balance for userId: {} is: {}", userId, user.getBalance());

        user.setBalance(user.getBalance() + Double.parseDouble(amount));
        userRepository.save(user);
        log.info("Updated balance for userId: {} is now: {}", userId, user.getBalance());

        // Create and save the transaction
        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setAmount(Double.valueOf(amount));
        transaction.setReferenceId(UUID.randomUUID().toString());
        transactionRepository.save(transaction);

        log.info("Transaction created for userId: {} with amount: {} and referenceId: {}", userId, amount, transaction.getReferenceId());

        return transaction;
    }
}
