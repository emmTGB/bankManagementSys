package com.kevin.bankmanagementsys.controller.api;

import com.kevin.bankmanagementsys.dto.request.AuthRequest;
import com.kevin.bankmanagementsys.dto.request.CreateAccountRequest;
import com.kevin.bankmanagementsys.dto.response.AccountResponse;
import com.kevin.bankmanagementsys.dto.response.PageResponse;
import com.kevin.bankmanagementsys.dto.response.TransactionResponse;
import com.kevin.bankmanagementsys.service.AccountService;
import com.kevin.bankmanagementsys.service.TransactionService;
import com.kevin.bankmanagementsys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/create")
    public ResponseEntity<String> createAccount(@RequestBody CreateAccountRequest createAccountRequest) {
        AuthRequest authRequest = createAccountRequest.getAuthRequest();
        AccountResponse accountResponse = createAccountRequest.getAccountResponse();

        try {
            if (!userService.authenticate(authRequest)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password.");
            }
            accountService.create(accountResponse);
            return ResponseEntity.status(HttpStatus.CREATED).body("Account created successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{accountId}/basic")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long accountId) {
        try {
            AccountResponse accountResponse = accountService.getAccount(accountId);
            return ResponseEntity.ok(accountResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{accountId}/details")
    public ResponseEntity<AccountResponse> getAccountWithAuth(@PathVariable Long accountId,
            @RequestBody AuthRequest authRequest,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        try {
            if (!userService.authenticate(authRequest)) {
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
            AccountResponse accountResponse = accountService.getAccountWithAuth(accountId);
            return ResponseEntity.ok(accountResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long accountId, @RequestBody AuthRequest authRequest,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid authentication.");
        }

        try {
            if (!userService.authenticate(authRequest)) {
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Password");
            }
            accountService.deleteAccount(accountId);
            return ResponseEntity.ok("Account deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{accountId}/transactions/{page}")
    public ResponseEntity<PageResponse<TransactionResponse>> getTransactions(@PathVariable Long accountId,
            @PathVariable int page) {
        try {
            PageResponse<TransactionResponse> responseBody = transactionService.getPageByAccountIdAll(accountId, page,
                    TransactionService.TRANSACTION_PAGE_SIZE);
            return ResponseEntity.ok(responseBody);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
