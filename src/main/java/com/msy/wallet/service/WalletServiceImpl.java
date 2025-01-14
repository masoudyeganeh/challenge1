package com.msy.wallet.service;

import com.msy.wallet.exception.ErrorCode;
import com.msy.wallet.exception.WalletServiceException;
import com.msy.wallet.model.Transaction;
import com.msy.wallet.model.User;
import com.msy.wallet.repository.TransactionRepository;
import com.msy.wallet.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class WalletServiceImpl implements WalletService {

    private final UserRepository userRepository;

    public WalletServiceImpl(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    private final TransactionRepository transactionRepository;


    @Override
    public User getBalance(Long userId) throws WalletServiceException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new WalletServiceException(ErrorCode.USER_NOT_FOUND, userId));
    }
    @Override
    @Transactional
    public Transaction addMoney(Long userId, Double amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new WalletServiceException(ErrorCode.USER_NOT_FOUND, userId));
        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);

        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setAmount(amount);
        transaction.setReferenceId(UUID.randomUUID().toString());
        transactionRepository.save(transaction);

        return transaction;
    }
}
