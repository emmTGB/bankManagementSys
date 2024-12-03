package com.kevin.bankmanagementsys.entity;

import com.kevin.bankmanagementsys.utils.AccountUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

//账户

@Entity
@Getter
@Setter
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 主键 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 账户所属用户（关联用户表）

    @Column(name = "account_number", unique = true, nullable = false)
    private String accountNumber;  // 账户号码，唯一

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;  // 账户类型，使用枚举类型，如储蓄账户，活期账户

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BankName bankName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status = AccountStatus.ACTIVE;  // 账户状态，默认为有效

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;  // 账户余额，默认为0

    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions;  // 账户的交易记录

//    @Version
//    private Long version;

    public String getAccountNumberWithoutAuth(){
        return AccountUtils.getMaskedAccountNumber(accountNumber);
    }

}
