package com.kevin.bankmanagementsys.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

//贷款

@Entity
@Getter
@Setter
@Table(name = "loans")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 主键 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 贷款申请人（关联用户表）

    @Column(nullable = false)
    private BigDecimal loanAmount;  // 贷款金额

    @Column(nullable = false)
    private BigDecimal interestRate;  // 利率

    @Column(nullable = false)
    private LocalDateTime loanDate = LocalDateTime.now();  // 贷款申请日期

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus loanStatus;  // 贷款状态（待审批、批准、拒绝、完成）

    @Column(nullable = true)
    private LocalDateTime repaymentStartDate;  // 还款开始日期

    @Column(nullable = true)
    private BigDecimal totalRepaymentAmount;  // 总还款金额

    @OneToMany(mappedBy = "loan")
    private List<LoanRepayment> repayments;  // 还款记录

    // Getter 和 Setter 省略
}

