package com.kevin.bankmanagementsys.controller.api;

import com.kevin.bankmanagementsys.dto.request.AccountStatusRequest;
import com.kevin.bankmanagementsys.dto.response.AccountResponse;
import com.kevin.bankmanagementsys.dto.response.EmployeeInfoResponse;
import com.kevin.bankmanagementsys.dto.response.PageResponse;
import com.kevin.bankmanagementsys.exception.user.UserNotFoundException;
import com.kevin.bankmanagementsys.service.AccountService;
import com.kevin.bankmanagementsys.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/account/list")
    public ResponseEntity<List<AccountResponse>> getAccounts() {
        try {
            List<AccountResponse> responseBody = accountService.getAll();
            return ResponseEntity.ok(responseBody);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/account/status")
    public ResponseEntity<String> updateStatus( @RequestBody AccountStatusRequest request) {
        try {
            accountService.updateStatus(request);
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeInfoResponse> getEmployee(@PathVariable Long id) {
        try {
            EmployeeInfoResponse response = employeeService.getEmployee(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.ok("Employee deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
