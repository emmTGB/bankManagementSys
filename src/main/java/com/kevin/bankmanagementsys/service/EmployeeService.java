package com.kevin.bankmanagementsys.service;

import com.kevin.bankmanagementsys.dto.request.EmployeeRegisterRequest;
import com.kevin.bankmanagementsys.dto.request.LoginRequest;
import com.kevin.bankmanagementsys.dto.response.EmployeeInfoResponse;
import com.kevin.bankmanagementsys.entity.Employee;
import com.kevin.bankmanagementsys.entity.EmployeeRole;
import com.kevin.bankmanagementsys.repository.EmployeeDAO;
import com.kevin.bankmanagementsys.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeDAO employeeDAO;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    public void register(EmployeeRegisterRequest registerRequest) throws RuntimeException {
        if (employeeDAO.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        Employee employee = new Employee();
        employee.setUsername(registerRequest.getUsername());
        employee.setFullName(registerRequest.getFullName());
        employee.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        employee.setRole(EmployeeRole.valueOf(registerRequest.getRole()));
        employee.setEmail(registerRequest.getEmail());
        employee.setPhone(registerRequest.getPhone());

        employeeDAO.save(employee);
    }

    public Map<String, String> login(LoginRequest loginRequest) throws RuntimeException {
        Employee employee = employeeDAO.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Username not found"));

        if(!passwordEncoder.matches(loginRequest.getPassword(), employee.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtTokenProvider.createToken(loginRequest.getUsername());

        Map<String, String> map = new HashMap<String, String>();
        map.put("token", token);
        map.put("id", String.valueOf(employee.getId()));

        return map;
    }

    public EmployeeInfoResponse getEmployee(Long id) throws RuntimeException {
        Employee employee = employeeDAO.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
        return new EmployeeInfoResponse(employee);
    }
}
