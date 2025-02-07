package com.msy.wallet.service;

import com.msy.wallet.exception.ErrorCode;
import com.msy.wallet.exception.WalletServiceException;
import com.msy.wallet.model.Transaction;
import com.msy.wallet.model.User;
import com.msy.wallet.repository.TransactionRepository;
import com.msy.wallet.repository.UserRepository;
import com.msy.wallet.scheduler.DailyTransactionScheduler;
import com.msy.wallet.util.ReferenceIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletServiceImpl implements WalletService {

    private static final Logger log = LoggerFactory.getLogger(DailyTransactionScheduler.class);
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    private final ReferenceIdGenerator referenceIdGenerator;

    public WalletServiceImpl(UserRepository userRepository, TransactionRepository transactionRepository, ReferenceIdGenerator referenceIdGenerator) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.referenceIdGenerator = referenceIdGenerator;
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
    public Transaction addMoney(Long userId, Long amount) {
        log.debug("Entering addMoney for userId: {} with amount: {}", userId, amount);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found for userId: {}", userId);
                    return new WalletServiceException(ErrorCode.USER_NOT_FOUND, userId);
                });

        log.info("Current balance for userId: {} is: {}", userId, user.getBalance());

        if(user.getBalance() + amount < 0) {
            throw new WalletServiceException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);
        log.info("Updated balance for userId: {} is now: {}", userId, user.getBalance());

        Transaction transaction = new Transaction()
                .setUser(user)
                .setAmount(amount)
                .setReferenceId(referenceIdGenerator.generateReferenceId());
        transactionRepository.save(transaction);

        log.info("Transaction created for userId: {} with amount: {} and referenceId: {}", userId, amount, transaction.getReferenceId());

        return transaction;
    }
}
