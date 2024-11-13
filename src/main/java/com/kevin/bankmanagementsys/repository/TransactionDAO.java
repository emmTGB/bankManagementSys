package com.kevin.bankmanagementsys.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import com.kevin.bankmanagementsys.entity.Transaction;

public interface TransactionDAO extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountId(Long accountId);

    List<Transaction> findByToAccountId(Long toAccountId);

    @NonNull
    Optional<Transaction> findById(@NonNull Long id);

    Page<Transaction> findByAccountIdOrToAccountId(Long accountId, Long toAccountId, Pageable pageable);

    long count();
}
