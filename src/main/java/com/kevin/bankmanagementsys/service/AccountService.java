package com.kevin.bankmanagementsys.service;

import com.kevin.bankmanagementsys.dto.request.CreateAccountRequest;
import com.kevin.bankmanagementsys.dto.response.AccountResponse;
import com.kevin.bankmanagementsys.dto.response.PageResponse;
import com.kevin.bankmanagementsys.entity.Account;
import com.kevin.bankmanagementsys.entity.AccountStatus;
import com.kevin.bankmanagementsys.entity.AccountType;
import com.kevin.bankmanagementsys.entity.User;
import com.kevin.bankmanagementsys.exception.user.UserNotFoundException;
import com.kevin.bankmanagementsys.repository.AccountDAO;
import com.kevin.bankmanagementsys.repository.UserDAO;
import com.kevin.bankmanagementsys.utils.AccountUtils;
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
public class AccountService {
    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private UserDAO userDAO;

    public void create(CreateAccountRequest createAccountRequest) throws RuntimeException {
        User user = userDAO.findById(createAccountRequest.getUserId())
                .orElseThrow(UserNotFoundException::new);

        String accountNumber;
        int entry = 0;
        do {
            entry += 1;
            accountNumber = AccountUtils.generateAccountNumber();
            if (entry > 5) {
                throw new RuntimeException("Can not generate account number");
            }
        } while (accountDAO.existsByAccountNumber(accountNumber));

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setUser(user);
        account.setAccountType(AccountType.valueOf(createAccountRequest.getAccountType()));
        account.setBalance(BigDecimal.ZERO);
        account.setStatus(AccountStatus.ACTIVE);

        accountDAO.save(account);
    }

    public AccountResponse getAccount(Long accountId) throws RuntimeException {
        Account account = accountDAO.findById(accountId).orElseThrow(RuntimeException::new);

        AccountResponse accountResponse = new AccountResponse(account);
        // accountDTO.setBalance(account.getBalance());

        return accountResponse;
    }

    public AccountResponse getAccountWithAuth(Long accountId) throws RuntimeException {
        Account account = accountDAO.findById(accountId).orElseThrow(RuntimeException::new); // todo

        AccountResponse accountResponse = new AccountResponse(account);

        accountResponse.setAccountNumber(account.getAccountNumber());
        accountResponse.setBalance(account.getBalance());

        return accountResponse;
    }

    public PageResponse<AccountResponse> getPageByUserIdAll(Long userId, int page) throws RuntimeException {
        User user = userDAO.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "id"));
        Page<Account> pageAccounts = accountDAO.findByUser(user, pageable);
        List<Account> accounts = pageAccounts.getContent();
        List<AccountResponse> accountResponses = accounts.stream().map(AccountResponse::new)
                .collect(Collectors.toList());
        return new PageResponse<>(accountResponses,
                pageAccounts.getNumber(), pageAccounts.getTotalPages(),
                pageAccounts.getSize(), pageAccounts.getTotalElements());
    }

    public void deleteAccount(Long id) throws RuntimeException {
        if (accountDAO.existsById(id))
            accountDAO.deleteById(id);
        throw new RuntimeException("Account Not Found");
    }
}
