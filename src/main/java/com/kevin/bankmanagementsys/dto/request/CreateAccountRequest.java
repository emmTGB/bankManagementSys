package com.kevin.bankmanagementsys.dto.request;

import com.kevin.bankmanagementsys.dto.response.AccountResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateAccountRequest {
    @NotNull(message = "User ID cannot be null")
    private Long userId; // 账户所属用户的ID

    @NotNull(message = "Account type cannot be null")
    @Pattern(regexp = "SAVING|CHECKING|LOAN", message = "Invalid account type")
    private String accountType; // 账户类型，使用枚举类型，如储蓄账户，活期账户

    @NotNull(message = "Auth info cannot be null")
    private AuthRequest authRequest;
}