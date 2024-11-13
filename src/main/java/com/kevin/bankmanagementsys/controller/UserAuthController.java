package com.kevin.bankmanagementsys.controller;

import com.kevin.bankmanagementsys.dto.request.LoginDTO;
import com.kevin.bankmanagementsys.dto.request.UserRegisterDTO;
import com.kevin.bankmanagementsys.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth/user")
public class UserAuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegisterDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessages = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> errorMessages.append(error.getDefaultMessage()).append("\n"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages.toString());
        }

        try {
            userService.register(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()); // todo 仍需细分 如conflict
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessages = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> errorMessages.append(error.getDefaultMessage()).append("\n"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages.toString());
        }

        try {
            Map<String, String> tokens = userService.login(loginDTO);
            return ResponseEntity.ok()
                    .header("Access-Token", tokens.get("accessToken"))
                    .header("Refresh-Token", tokens.get("refreshToken"))
                    .body("Login successful");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(HttpServletRequest request) {
        try {
            String refreshToken = request.getHeader("Refresh-Token");
            if (refreshToken == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing Refresh-Token");
            }
            Map<String, String> tokens = userService.refresh(refreshToken);
            return ResponseEntity.ok()
                    .header("Access-Token", tokens.get("accessToken"))
                    .header("Refresh-Token", tokens.get("refreshToken"))
                    .body("Tokens refreshed successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String refreshToken = request.getHeader("Refresh-Token");
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing Refresh-Token");
        }
        userService.logout(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body("Logout successfully");
    }
}
