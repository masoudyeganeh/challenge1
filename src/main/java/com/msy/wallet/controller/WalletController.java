package com.msy.wallet.controller;

import com.msy.wallet.model.Transaction;
import com.msy.wallet.model.User;
import com.msy.wallet.service.WalletService;
import com.msy.wallet.service.validation.ValidUserId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.springframework.format.annotation.NumberFormat;
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
    public ResponseEntity<User> getBalance(@RequestParam @ValidUserId Long userId) {
        User user = walletService.getBalance(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/addMoney")
    public ResponseEntity<Transaction> addMoney(@RequestParam @ValidUserId Long userId,
                                                @RequestParam Double amount) {
        Transaction transaction = walletService.addMoney(userId, amount);
        return ResponseEntity.ok(transaction);
    }
}
