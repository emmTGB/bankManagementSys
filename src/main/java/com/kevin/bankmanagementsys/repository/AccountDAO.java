package com.kevin.bankmanagementsys.repository;

import com.kevin.bankmanagementsys.entity.Account;
import com.kevin.bankmanagementsys.entity.AccountType;
import com.kevin.bankmanagementsys.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountDAO extends JpaRepository<Account, Integer> {
    Account findByAccountNumber(String accountNumber);

    List<Account> findByUser(User user);

    List<Account> findByAccountType(AccountType accountType);
}
