package com.kevin.bankmanagementsys.repository;

import com.kevin.bankmanagementsys.entity.Account;
import com.kevin.bankmanagementsys.entity.AccountType;
import com.kevin.bankmanagementsys.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountDAO extends JpaRepository<Account, Integer> {
    Optional<Account> findByAccountNumber(String accountNumber);

    Optional<Account> findById(Long id);

    boolean existsByAccountNumber(String accountNumber);

    List<Account> findByUser(User user);

    List<Account> findByAccountType(AccountType accountType);
}
