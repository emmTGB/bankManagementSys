package com.kevin.bankmanagementsys.controller;

/*
处理交易请求
POST /transactions/deposit: 进行存款操作。
POST /transactions/withdraw: 进行取款操作。
POST /transactions/transfer: 进行转账操作。
GET /transactions/{id}: 查看交易记录。
 */

import com.kevin.bankmanagementsys.dto.request.DepositDTO;
import com.kevin.bankmanagementsys.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody DepositDTO depositDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(
                    bindingResult.getAllErrors().stream()
                            .map(ObjectError::getDefaultMessage)
                            .reduce((msg1, msg2) -> msg1 + ";\n" + msg2)
                            .orElse("Invalid request data"));
        }

        try{
            transactionService.deposit(depositDTO);
            return ResponseEntity.ok("Deposit successful");
        }catch (RuntimeException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
