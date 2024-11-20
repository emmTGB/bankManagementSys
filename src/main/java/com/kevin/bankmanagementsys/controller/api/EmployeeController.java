package com.kevin.bankmanagementsys.controller.api;

import com.kevin.bankmanagementsys.dto.response.EmployeeInfoResponse;
import com.kevin.bankmanagementsys.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeInfoResponse> getEmployee(@PathVariable("id") Long id){
        try{
            EmployeeInfoResponse response = employeeService.getEmployee(id);
            return ResponseEntity.ok(response);
        }catch (RuntimeException e){
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
