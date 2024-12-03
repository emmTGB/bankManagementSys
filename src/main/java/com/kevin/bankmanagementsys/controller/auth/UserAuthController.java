package com.kevin.bankmanagementsys.controller.auth;

import com.kevin.bankmanagementsys.dto.request.AuthRequest;
import com.kevin.bankmanagementsys.dto.request.LoginRequest;
import com.kevin.bankmanagementsys.dto.request.UserRegisterRequest;
import com.kevin.bankmanagementsys.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth/user")
public class UserAuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegisterRequest userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessages = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> errorMessages.append(error.getDefaultMessage()).append("\n"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages.toString());
        }

        try {
            System.out.println(1);
            userService.register(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()); // todo 仍需细分 如conflict
        }
    }

    @Operation(summary = "用户登录")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "登陆成功", headers = {
                    @Header(name = "Access-Token", description = "访问令牌"),
                    @Header(name = "Refresh-Token", description = "刷新令牌"),
                    @Header(name = "ID", description = "用户id")
            }),
            @ApiResponse(responseCode = "401", description = "登陆失败")
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginDTO, BindingResult bindingResult) {
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
                    .header("ID", tokens.get("id"))
                    .body("Login successful");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @Operation(summary = "刷新令牌", description = "提供 `Refresh-Token` 来获取新的 `Access-Token`, 并在必要时更新 `Refresh-Token`")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "令牌刷新成功",
                    headers = {
                            @Header(name = "Access-Token", description = "新的访问令牌"),
                            @Header(name = "Refresh-Token", description = "新的刷新令牌"),
                    }
            ),
            @ApiResponse(responseCode = "401", description = "未授权")
    })
    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(
            @Parameter(description = "用户的刷新令牌", required = true)
            @RequestHeader("Refresh-Token") String refreshToken
    ) {
        try {
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
    public ResponseEntity<String> logout(
            @Parameter(description = "用户的刷新令牌", required = true)
            @RequestHeader(name = "Refresh-Token") String refreshToken) {
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing Refresh-Token");
        }
        userService.logout(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body("Logout successfully");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestBody AuthRequest authRequest, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            StringBuilder errorMessages = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> errorMessages.append(error.getDefaultMessage()).append("\n"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages.toString());
        }

        try{
            userService.authenticate(authRequest);
            return ResponseEntity.status(HttpStatus.OK).body("User authenticated successfully");
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
