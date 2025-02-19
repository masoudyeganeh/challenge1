package com.msy.wallet.controller;

import com.msy.wallet.model.Role;
import com.msy.wallet.model.Transaction;
import com.msy.wallet.model.User;
import com.msy.wallet.repository.RoleRepository;
import com.msy.wallet.repository.UserRepository;
import com.msy.wallet.service.AuthService;
import com.msy.wallet.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/wallet")
@Validated
public class WalletController {

    private final WalletService walletService;

    private final AuthService authService;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public WalletController(WalletService walletService, AuthService authService, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.walletService = walletService;
        this.authService = authService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/getBalance")
    public ResponseEntity<User> getBalance(@RequestParam Long userId) {
        User user = walletService.getBalance(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/addMoney")
    public ResponseEntity<Transaction> addMoney(@RequestParam Long userId,
                                                @RequestParam Long amount) {
            Transaction transaction = walletService.addMoney(userId, amount);
            return ResponseEntity.ok(transaction);
    }

    @PostMapping("/auth/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        return authService.authenticate(username, password);
    }

    @PostMapping("/auth/register")
    public String registerUser(@RequestParam String username, @RequestParam String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            return "Username already taken!";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        Role userRole = roleRepository.findByName("USER");
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);
        }

        userRepository.save(user);

        return "User registered successfully!";
    }
}
