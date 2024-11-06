package com.kevin.bankmanagementsys.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//还款记录

@Entity
@Setter
@Getter
@Table(name = "loan_repayments")
public class LoanRepayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 主键 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;  // 关联贷款

    @Column(nullable = false)
    private BigDecimal repaymentAmount;  // 本次还款金额

    @Column(nullable = false)
    private LocalDateTime repaymentDate = LocalDateTime.now();  // 还款日期

    // Getter 和 Setter 省略
}
