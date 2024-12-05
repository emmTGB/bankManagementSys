package com.kevin.bankmanagementsys.controller.api;

import com.kevin.bankmanagementsys.dto.request.AuthRequest;
import com.kevin.bankmanagementsys.dto.request.TransactionRequest;
import com.kevin.bankmanagementsys.service.TransactionService;
import com.kevin.bankmanagementsys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;
    @Autowired
    private View error;

    private ResponseEntity<String> processTransaction(TransactionRequest transactionRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(
                    bindingResult.getAllErrors().stream()
                            .map(ObjectError::getDefaultMessage)
                            .reduce((msg1, msg2) -> msg1 + ";\n" + msg2)
                            .orElse("Invalid request data"));
        }

        AuthRequest authRequest = transactionRequest.getAuthRequest();
        if(authRequest == null){
            return ResponseEntity.badRequest().body("Invalid auth request");
        }

        try{
            userService.authenticate(authRequest);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }

        return null;
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody TransactionRequest transactionRequest, BindingResult bindingResult){
        ResponseEntity<String> result = processTransaction(transactionRequest, bindingResult);
        if(result != null){
            return result;
        }

        try{
            transactionService.deposit(transactionRequest);
            return ResponseEntity.ok("Deposit successful");
        }catch (RuntimeException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/withdrawal")
    public ResponseEntity<String> withdrawal(@RequestBody TransactionRequest transactionRequest, BindingResult bindingResult){
        ResponseEntity<String> result = processTransaction(transactionRequest, bindingResult);
        if(result != null){
            return result;
        }

        try{
            transactionService.withdrawal(transactionRequest);
            return ResponseEntity.ok("Deposit successful");
        }catch (RuntimeException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransactionRequest transactionRequest, BindingResult bindingResult){
        ResponseEntity<String> result = processTransaction(transactionRequest, bindingResult);
        if(result != null){
            return result;
        }

        try{
            transactionService.transfer(transactionRequest);
            return ResponseEntity.ok("Transfer successful");
        } catch (RuntimeException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
