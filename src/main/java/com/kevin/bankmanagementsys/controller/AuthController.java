package com.kevin.bankmanagementsys.controller;

import com.kevin.bankmanagementsys.dto.LoginDTO;
import com.kevin.bankmanagementsys.dto.UserDTO;
import com.kevin.bankmanagementsys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/register")
    public ResponseEntity<String> userRegister(@RequestBody UserDTO userDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            StringBuilder errorMessages = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> errorMessages.append(error.getDefaultMessage()).append("\n"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages.toString());
        }

        try{
            userService.register(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());  // todo 仍需细分 如conflict
        }
    }

    @PostMapping("/user/login")
    public ResponseEntity<Map<String, String>> userLogin(@RequestBody LoginDTO loginDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            StringBuilder errorMessages = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> errorMessages.append(error.getDefaultMessage()).append("\n"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", errorMessages.toString()));
        }

        try{
            String token = userService.login(loginDTO);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", e.getMessage()));
        }
    }
}
