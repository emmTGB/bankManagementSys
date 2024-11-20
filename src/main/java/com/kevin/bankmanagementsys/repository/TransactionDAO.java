package com.kevin.bankmanagementsys.repository;

import com.kevin.bankmanagementsys.entity.Account;
import com.kevin.bankmanagementsys.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface TransactionDAO extends JpaRepository<Transaction, Long> {

    @NonNull
    Optional<Transaction> findById(@NonNull Long id);

    Page<Transaction> findByAccountOrAccountReceive(Account account, Account accountReceive, Pageable pageable);

    long count();
}
