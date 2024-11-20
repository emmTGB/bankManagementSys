package com.kevin.bankmanagementsys.controller.auth;

import com.kevin.bankmanagementsys.dto.request.EmployeeRegisterRequest;
import com.kevin.bankmanagementsys.dto.request.LoginRequest;
import com.kevin.bankmanagementsys.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/auth/employee")
public class EmployeeAuthController {

    @Autowired
    private EmployeeService employeeService;

    @Operation(summary = "员工注册")
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody EmployeeRegisterRequest registerRequest, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    bindingResult.getAllErrors().stream()
                            .map(ObjectError::getDefaultMessage)
                            .reduce((msg1, msg2) -> msg1 + ";\n" + msg2)
                            .orElse("Invalid request data"));
        }

        try {
            employeeService.register(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("Employee Register Success");
        }catch (RuntimeException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    @Operation(summary = "职员登录")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "登录成功", headers = {
                    @Header(name = "Authorization", description = "登录令牌"),
                    @Header(name = "ID", description = "职员id")
            }),
            @ApiResponse(responseCode = "401", description = "登陆失败")
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    bindingResult.getAllErrors().stream()
                            .map(ObjectError::getDefaultMessage)
                            .reduce((msg1, msg2) -> msg1 + ";\n" + msg2)
                            .orElse("Invalid request data"));
        }

        try{
            Map<String, String> result = employeeService.login(loginRequest);
            return ResponseEntity.ok()
                    .header("Authorization", result.get("token"))
                    .header("ID", result.get("id"))
                    .body("Login Success");
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    
}
