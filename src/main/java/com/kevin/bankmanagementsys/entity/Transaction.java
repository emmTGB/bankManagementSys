package com.kevin.bankmanagementsys.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//交易记录

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 主键 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;  // 关联账户

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_account_id", nullable = true)
    private Account accountReceive;  // 关联接收账户（仅转款时）

    @Column(nullable = false)
    private BigDecimal amount;  // 交易金额

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;  // 交易类型（存款、取款、转账）

    @Column(nullable = false)
    private LocalDateTime transactionDate = LocalDateTime.now();  // 交易时间

    @Column(nullable = true)
    private String description;  // 交易描述（例如转账备注）

    // Getter 和 Setter 省略
}


