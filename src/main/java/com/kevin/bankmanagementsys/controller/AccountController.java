package com.kevin.bankmanagementsys.controller;

/*
账户管理
POST /accounts: 开设新账户。
GET /accounts/{id}: 获取账户信息。
PUT /accounts/{id}: 更新账户信息（如修改密码）。
DELETE /accounts/{id}: 删除账户。
GET /accounts/{id}/transactions: 获取账户交易记录。
 */

import com.kevin.bankmanagementsys.dto.AccountDTO;
import com.kevin.bankmanagementsys.dto.AuthDTO;
import com.kevin.bankmanagementsys.dto.CreateAccountRequest;
import com.kevin.bankmanagementsys.service.AccountService;
import com.kevin.bankmanagementsys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

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
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
    public ResponseEntity<AccountDTO> getAccountWithAuth(@PathVariable Long accountId, @RequestBody AuthDTO authDTO, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        try{
            if(!userService.authenticate(authDTO)) {
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
            AccountDTO accountDTO = accountService.getAccountWithAuth(accountId);
            return ResponseEntity.ok(accountDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
