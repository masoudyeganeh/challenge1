package com.msy.wallet.service;

import com.msy.wallet.exception.WalletServiceException;
import com.msy.wallet.model.User;

public interface WalletService {
    User getBalance(Long userId) throws WalletServiceException;;
    String addMoney(Long userId, Double amount);
}
