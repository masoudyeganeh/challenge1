package com.msy.wallet.controller;

import com.msy.wallet.model.User;
import com.msy.wallet.service.WalletService;
import lombok.NonNull;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/getBalance")
    public ResponseEntity<User> getBalance(@RequestParam @NonNull Long userId) {
        User user = walletService.getBalance(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/addMoney")
    public String addMoney(@RequestParam Long userId, @RequestParam Double amount) {
        return walletService.addMoney(userId, amount);
    }
}
