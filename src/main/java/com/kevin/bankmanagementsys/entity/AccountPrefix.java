package com.kevin.bankmanagementsys.entity;

import jakarta.persistence.*;
import org.springframework.context.annotation.Primary;

@Entity
@Table(name = "account-prefix")
public class AccountPrefix {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 主键 ID

    @Column(nullable = false)
    private BankName bankName;

    @Column(nullable = false)
    private AccountType accountType;

    @Column(nullable = false)
    private String prefix;
}
