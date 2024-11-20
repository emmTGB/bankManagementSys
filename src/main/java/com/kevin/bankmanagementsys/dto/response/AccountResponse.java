package com.kevin.bankmanagementsys.dto.response;

import com.kevin.bankmanagementsys.entity.Account;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountResponse {

    private Long id;

    @NotNull(message = "User ID cannot be null")
    private Long userId; // 账户所属用户的ID

    @NotNull(message = "Account number cannot be null")
    @Size(min = 10, max = 20, message = "Account number must be between 10 and 20 characters")
    private String accountNumber; // 账户号码，唯一

    @NotNull(message = "Account type cannot be null")
    @Pattern(regexp = "SAVING|CHECKING|LOAN", message = "Invalid account type")
    private String accountType; // 账户类型，使用枚举类型，如储蓄账户，活期账户

    @NotNull(message = "Account status cannot be null")
    @Pattern(regexp = "ACTIVE|FROZEN|CLOSED", message = "Invalid account status")
    private String status;

    private BigDecimal balance;

    // 如果您需要返回交易记录的话可以添加，但一般情况下会避免返回大量的关联数据
    public AccountResponse(Account account) {
        this.id = account.getId();
        this.accountNumber = account.getAccountNumberWithoutAuth();
        this.accountType = account.getAccountType().name();
        this.balance = null;
        this.status = account.getStatus().name();
        this.userId = account.getUser().getId();
    }
}
