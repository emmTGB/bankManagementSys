package com.kevin.bankmanagementsys.service;

import com.kevin.bankmanagementsys.dto.AccountDTO;
import com.kevin.bankmanagementsys.dto.UserDTO;
import com.kevin.bankmanagementsys.entity.Account;
import com.kevin.bankmanagementsys.entity.AccountStatus;
import com.kevin.bankmanagementsys.entity.AccountType;
import com.kevin.bankmanagementsys.entity.User;
import com.kevin.bankmanagementsys.exception.user.UserNotFoundException;
import com.kevin.bankmanagementsys.repository.AccountDAO;
import com.kevin.bankmanagementsys.repository.UserDAO;
import com.kevin.bankmanagementsys.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountService {
    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private UserDAO userDAO;

    public Account create(AccountDTO accountDTO) throws RuntimeException {
        User user = userDAO.findById(accountDTO.getUserId())
                .orElseThrow(UserNotFoundException::new);

        String accountNumber;
        int entry = 0;
        do {
            entry += 1;
            accountNumber = AccountUtils.generateAccountNumber();
            if (entry > 5) {
                throw new RuntimeException("Can not generate account number");
            }
        } while (!accountDAO.existsByAccountNumber(accountNumber));

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setUser(user);
        account.setAccountType(AccountType.valueOf(accountDTO.getAccountType()));
        account.setBalance(BigDecimal.ZERO);
        account.setStatus(AccountStatus.ACTIVE);

        return accountDAO.save(account);
    }

    public AccountDTO getAccount(Long accountId) throws RuntimeException {
        Account account = accountDAO.findById(accountId).orElseThrow(RuntimeException::new);

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUserId(account.getUser().getId());
        accountDTO.setId(account.getId());
        accountDTO.setAccountType(account.getAccountType().name());
        accountDTO.setStatus(account.getStatus().name());

        accountDTO.setAccountNumber(account.getAccountNumberWithoutAuth());
        // accountDTO.setBalance(account.getBalance());

        return accountDTO;
    }

    public AccountDTO getAccountWithAuth(Long accountId) throws RuntimeException {
        Account account = accountDAO.findById(accountId).orElseThrow(RuntimeException::new);

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUserId(account.getUser().getId());
        accountDTO.setId(account.getId());
        accountDTO.setAccountType(account.getAccountType().name());
        accountDTO.setStatus(account.getStatus().name());

        accountDTO.setAccountNumber(account.getAccountNumber());
        accountDTO.setBalance(account.getBalance());

        return accountDTO;
    }
}
