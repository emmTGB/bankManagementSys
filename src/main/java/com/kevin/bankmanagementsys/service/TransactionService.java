package com.kevin.bankmanagementsys.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.kevin.bankmanagementsys.dto.request.DepositDTO;
import com.kevin.bankmanagementsys.entity.Account;
import com.kevin.bankmanagementsys.repository.AccountDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.kevin.bankmanagementsys.dto.response.PageDTO;
import com.kevin.bankmanagementsys.dto.response.TransactionDTO;
import com.kevin.bankmanagementsys.entity.Transaction;
import com.kevin.bankmanagementsys.repository.TransactionDAO;

@Service
public class TransactionService {

    public static final int TRANSACTION_PAGE_SIZE = 10;

    @Autowired
    TransactionDAO transactionDAO;

    @Autowired
    AccountDAO accountDAO;

    public PageDTO<TransactionDTO> getPageByAccountIdAll(Long accountId, int page, int size) throws RuntimeException {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "transactionDate"));
        Page<Transaction> pageTransactions = transactionDAO.findByAccountIdOrToAccountId(accountId, accountId,
                pageable);
        List<Transaction> transactions = pageTransactions.getContent();
        List<TransactionDTO> transactionDTOs = transactions.stream().map(TransactionDTO::new)
                .collect(Collectors.toList());
        return new PageDTO<TransactionDTO>(transactionDTOs, pageTransactions.getNumber(),
                pageTransactions.getTotalPages(),
                pageTransactions.getSize(), pageTransactions.getTotalElements());
    }

    public void deposit(DepositDTO depositDTO) throws RuntimeException{
        Account account = accountDAO.findByAccountNumber(depositDTO.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        BigDecimal amount = depositDTO.getAmount();
        LocalDateTime transactionDate = depositDTO.getTransactionDate();
        String description = depositDTO.getDescription();

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAccountReceive(null);
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setTransactionDate(transactionDate);

        transactionDAO.save(transaction);
    }
}
