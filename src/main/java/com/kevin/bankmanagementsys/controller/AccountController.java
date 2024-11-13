package com.kevin.bankmanagementsys.controller;

import com.kevin.bankmanagementsys.dto.request.AuthDTO;
import com.kevin.bankmanagementsys.dto.request.CreateAccountRequest;
import com.kevin.bankmanagementsys.dto.response.AccountDTO;
import com.kevin.bankmanagementsys.dto.response.PageDTO;
import com.kevin.bankmanagementsys.dto.response.TransactionDTO;
import com.kevin.bankmanagementsys.entity.Transaction;
import com.kevin.bankmanagementsys.service.AccountService;
import com.kevin.bankmanagementsys.service.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<String> createAccount(@RequestBody CreateAccountRequest createAccountRequest) {
        AuthDTO authDTO = createAccountRequest.getAuthDTO();
        AccountDTO accountDTO = createAccountRequest.getAccountDTO();

        try {
            if (!userService.authenticate(authDTO)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password.");
            }
            accountService.create(accountDTO);
            return ResponseEntity.ok("Account created successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{accountId}/basic")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable Long accountId) {
        try {
            AccountDTO accountDTO = accountService.getAccount(accountId);
            return ResponseEntity.ok(accountDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{accountId}/details")
    public ResponseEntity<AccountDTO> getAccountWithAuth(@PathVariable Long accountId, @RequestBody AuthDTO authDTO,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        try {
            if (!userService.authenticate(authDTO)) {
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
            AccountDTO accountDTO = accountService.getAccountWithAuth(accountId);
            return ResponseEntity.ok(accountDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long accountId, @RequestBody AuthDTO authDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid authentication.");
        }

        try {
            if (!userService.authenticate(authDTO)) {
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Password");
            }
            accountService.deleteAccount(accountId);
            return ResponseEntity.ok("Account deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{accountId}/transactions/{page}")
    public ResponseEntity<PageDTO<TransactionDTO>>  getTransactions(@PathVariable Long accountId, @PathVariable int page) {
        try{
            PageDTO<TransactionDTO> responseBody =  accountService.getTransactions(accountId, page);
            return ResponseEntity.ok(responseBody);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
