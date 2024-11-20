package com.kevin.bankmanagementsys.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//交易记录

@Entity
@Getter
@Setter
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 主键 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_number")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_account_number")
    private Account accountReceive;

    @Column(nullable = false)
    private BigDecimal amount;  // 交易金额

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;  // 交易类型（存款、取款、转账）

    @Column(nullable = false)
    private LocalDateTime transactionDate = LocalDateTime.now();  // 交易时间

    @Column()
    private String description;  // 交易描述（例如转账备注）

    // Getter 和 Setter 省略
}


