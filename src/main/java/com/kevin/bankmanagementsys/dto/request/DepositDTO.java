package com.kevin.bankmanagementsys.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class DepositDTO {

    @NotNull(message = "Account Number Can Not Be Null")
    private String AccountNumber;

    @NotNull(message = "Amount Can Not Be Null")
    private BigDecimal Amount;

    @NotNull(message = "Transaction Type Can Not Be Null")
    private String transactionType;  // 交易类型（存款、取款、转账）

    @NotNull(message = "Transaction Date Can Not Be Null")
    private LocalDateTime transactionDate;  // 交易时间

    private String description;  // 交易描述（例如转账备注）
}
