package com.kevin.bankmanagementsys.repository;

import com.kevin.bankmanagementsys.entity.Account;
import com.kevin.bankmanagementsys.entity.Transaction;
import com.kevin.bankmanagementsys.entity.User;

import io.lettuce.core.dynamic.annotation.Param;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface TransactionDAO extends JpaRepository<Transaction, Long> {

    @NonNull
    Optional<Transaction> findById(@NonNull Long id);

//    @Query(
//        "select t from transactions t " +
//                "join accounts c " +
//                "on t.account_number = c.account_number or t.to_account_number = c.account_number " +
//                "where c.user_id = :#{#user.id}"
//    )
//    Page<Transaction> findByUser(@Param("user") User user, Pageable pageable);

    Page<Transaction> findByAccountOrAccountReceive(Account account, Account accountReceive, Pageable pageable);

    long count();
}
