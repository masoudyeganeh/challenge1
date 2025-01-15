package com.msy.wallet.controller;

import com.msy.wallet.model.Transaction;
import com.msy.wallet.model.User;
import com.msy.wallet.service.WalletService;
import com.msy.wallet.service.validation.UserIdRequired;
import com.msy.wallet.service.validation.ValidAmount;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
@Validated
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/getBalance")
    public ResponseEntity<User> getBalance(@RequestParam @UserIdRequired Long userId) {
        User user = walletService.getBalance(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/addMoney")
    public ResponseEntity<Transaction> addMoney(@RequestParam Long userId,
                                                @RequestParam String amount) {
        Transaction transaction = walletService.addMoney(userId, amount);
        return ResponseEntity.ok(transaction);
    }
}
