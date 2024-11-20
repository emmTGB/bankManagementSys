package com.kevin.bankmanagementsys.dto.response;

import com.kevin.bankmanagementsys.entity.Employee;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeInfoResponse {

    public EmployeeInfoResponse(Employee employee) {
        this.id = employee.getId();
        this.username = employee.getUsername();
        this.fullName = employee.getFullName();
        this.role = employee.getRole().name();
        this.email = employee.getEmail();
        this.phone = employee.getPhone();
    }

    private Long id;

    @NotNull(message = "Username can not be null")
    private String username;

    @NotNull(message = "Full name can not be null")
    private String fullName;

    @NotNull(message = "Employee role cannot be null")
    @Pattern(regexp = "MANAGER|CASHIER|CLERK", message = "Invalid employee role")
    private String role;

    @Email(message = "Email must be valid")
    private String email;
    private String phone;
}
