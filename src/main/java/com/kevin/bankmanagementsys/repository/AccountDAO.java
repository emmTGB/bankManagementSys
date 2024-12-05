package com.kevin.bankmanagementsys.repository;

import com.kevin.bankmanagementsys.entity.Account;
import com.kevin.bankmanagementsys.entity.AccountStatus;
import com.kevin.bankmanagementsys.entity.AccountType;
import com.kevin.bankmanagementsys.entity.User;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountDAO extends JpaRepository<Account, Integer> {

    /**
     * 带悲观锁查询，用于更新余额等场景
     */
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Account> findByAccountNumber(String accountNumber);

    Optional<Account> findById(Long id);

    boolean existsById(Long id);

    boolean existsByAccountNumber(String accountNumber);

    List<Account> findByUser(User user);

    Page<Account> findByUserAndStatusNot(User user, AccountStatus status, Pageable pageable);

    List<Account> findByAccountType(AccountType accountType);

    void deleteById(Long id);
}
