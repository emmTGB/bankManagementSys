package com.kevin.bankmanagementsys.dto.response;

import com.kevin.bankmanagementsys.entity.Transaction;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionResponse {
    public TransactionResponse(Transaction transaction) {
        this.id = transaction.getId();
        this.accountNumber = transaction.getAccount().getAccountNumberWithoutAuth();
        this.toAccountNumber = transaction.getAccountReceive() != null
                ? transaction.getAccountReceive().getAccountNumberWithoutAuth()
                : null;
        this.amount = transaction.getAmount();
        this.transactionType = transaction.getTransactionType().name();
        this.transactionDate = transaction.getTransactionDate();
        this.description = transaction.getDescription();
    }

    private Long id;
    private String accountNumber;
    private String toAccountNumber;
    private BigDecimal amount; // 交易金额
    private String transactionType; // 交易类型（存款、取款、转账）
    private LocalDateTime transactionDate; // 交易时间
    private String description; // 交易描述（例如转账备注）
}
