package com.kevin.bankmanagementsys.service;

import com.kevin.bankmanagementsys.dto.request.TransactionRequest;
import com.kevin.bankmanagementsys.dto.response.PageResponse;
import com.kevin.bankmanagementsys.dto.response.TransactionResponse;
import com.kevin.bankmanagementsys.entity.Account;
import com.kevin.bankmanagementsys.entity.Transaction;
import com.kevin.bankmanagementsys.entity.TransactionType;
import com.kevin.bankmanagementsys.repository.AccountDAO;
import com.kevin.bankmanagementsys.repository.TransactionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    public static final int TRANSACTION_PAGE_SIZE = 10;

    @Autowired
    TransactionDAO transactionDAO;

    @Autowired
    AccountDAO accountDAO;

    // 按账户id获取所有交易记录，分页
    public PageResponse<TransactionResponse> getPageByAccountIdAll(Long accountId, int page, int size)
            throws RuntimeException {
        Account account = accountDAO.findById(accountId).orElseThrow(RuntimeException::new); // todo

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "transactionDate"));
        Page<Transaction> pageTransactions = transactionDAO.findByAccountOrAccountReceive(account, account,
                pageable);
        List<Transaction> transactions = pageTransactions.getContent();
        List<TransactionResponse> transactionResponses = transactions.stream().map(TransactionResponse::new)
                .collect(Collectors.toList());
        return new PageResponse<>(transactionResponses, pageTransactions.getNumber(),
                pageTransactions.getTotalPages(),
                pageTransactions.getSize(), pageTransactions.getTotalElements());
    }

    private Transaction buildTransaction(TransactionRequest transactionRequest, Account account,
            Account accountReceive) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAccountReceive(accountReceive);
        transaction.setTransactionType(TransactionType.valueOf(transactionRequest.getTransactionType()));
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setDescription(transactionRequest.getDescription());
        transaction.setTransactionDate(transactionRequest.getTransactionDate());
        return transaction;
    }

    public void deposit(TransactionRequest transactionRequest) throws RuntimeException {
        TransactionType transactionType = TransactionType.valueOf(transactionRequest.getTransactionType());
        if (transactionType != TransactionType.DEPOSIT) {
            throw new RuntimeException("TransactionType not supported");
        }

        Account account = accountDAO.findByAccountNumber(transactionRequest.getAccountNumber())
                .orElseThrow(RuntimeException::new);
        BigDecimal amount = transactionRequest.getAmount();
        account.setBalance(account.getBalance().add(amount));
        accountDAO.save(account); // 乐观锁保存
        // todo

        transactionDAO.save(buildTransaction(transactionRequest, account, null));
    }

    public void withdrawal(TransactionRequest transactionRequest) throws RuntimeException {
        TransactionType transactionType = TransactionType.valueOf(transactionRequest.getTransactionType());
        if (transactionType != TransactionType.WITHDRAWAL) {
            throw new RuntimeException("TransactionType not supported");
        }

        Account account = accountDAO.findByAccountNumber(transactionRequest.getAccountNumber())
                .orElseThrow(RuntimeException::new);
        BigDecimal amount = transactionRequest.getAmount();
        account.setBalance(account.getBalance().subtract(amount));
        accountDAO.save(account); // 乐观锁保存
        // todo

        transactionDAO.save(buildTransaction(transactionRequest, account, null));
    }

    public void transfer(TransactionRequest transactionRequest) throws RuntimeException {
        TransactionType transactionType = TransactionType.valueOf(transactionRequest.getTransactionType());
        if (transactionType != TransactionType.TRANSFER) {
            throw new RuntimeException("TransactionType not supported");
        }

        BigDecimal amount = transactionRequest.getAmount();
        Account account = accountDAO.findByAccountNumber(transactionRequest.getAccountNumber())
                .orElseThrow(RuntimeException::new);
        Account accountReceive = accountDAO.findByAccountNumber(transactionRequest.getToAccountNumber())
                .orElseThrow(RuntimeException::new);
        account.setBalance(account.getBalance().subtract(amount));
        accountReceive.setBalance(accountReceive.getBalance().add(amount));
        accountDAO.save(account);
        accountDAO.save(accountReceive);

        transactionDAO.save(buildTransaction(transactionRequest, account, accountReceive));
    }
}
