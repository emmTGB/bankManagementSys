package com.kevin.bankmanagementsys.dto.request;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ValidTransfer
public class TransactionRequest {

    @NotNull(message = "Account Number Can Not Be Null")
    private String accountNumber;

    private String toAccountNumber;

    @NotNull(message = "Amount Can Not Be Null")
    private BigDecimal amount;

    @NotNull(message = "Transaction Type Can Not Be Null")
    private String transactionType;  // 交易类型（存款、取款、转账）

    @NotNull(message = "Transaction Date Can Not Be Null")
    private LocalDateTime transactionDate;  // 交易时间

    private String description;  // 交易描述（例如转账备注）

    private AuthRequest authRequest;
}



@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TransferValidator.class)
@interface ValidTransfer {
    String message() default "To Account Number is required for transfer transactions";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

class TransferValidator implements ConstraintValidator<ValidTransfer, TransactionRequest> {
    @Override
    public boolean isValid(TransactionRequest request, ConstraintValidatorContext context) {
        // 如果交易类型为 "TRANSFER"，则 toAccountNumber 必须非空
        if ("TRANSFER".equalsIgnoreCase(request.getTransactionType())) {
            return request.getToAccountNumber() != null && !request.getToAccountNumber().isEmpty();
        }
        // 对于其他交易类型，toAccountNumber 可以为空
        return true;
    }
}
