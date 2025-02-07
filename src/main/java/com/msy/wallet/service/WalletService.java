package com.msy.wallet.service;

import com.msy.wallet.exception.WalletServiceException;
import com.msy.wallet.model.Transaction;
import com.msy.wallet.model.User;

public interface WalletService {
    User getBalance(Long userId) throws WalletServiceException;;
    Transaction addMoney(Long userId, Long amount);
}
