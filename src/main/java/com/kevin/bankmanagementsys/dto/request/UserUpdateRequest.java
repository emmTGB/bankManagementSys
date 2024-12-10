package com.kevin.bankmanagementsys.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    @NotNull(message = "Password cannot be null")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotNull(message = "Password cannot be null")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String newPassword;

    @NotNull(message = "Full name can not be null")
    @Size(min = 1, max = 50, message = "Username must be between 1 and 50 characters")
    private String fullName;

    @Email(message = "Email must be valid")
    private String email;
    private String phone;
}
