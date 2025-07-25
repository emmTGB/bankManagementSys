package com.kevin.bankmanagementsys.service;

import com.kevin.bankmanagementsys.dto.request.AccountStatusRequest;
import com.kevin.bankmanagementsys.dto.request.CreateAccountRequest;
import com.kevin.bankmanagementsys.dto.response.AccountResponse;
import com.kevin.bankmanagementsys.dto.response.AccListItem;
import com.kevin.bankmanagementsys.dto.response.ListResponse;
import com.kevin.bankmanagementsys.dto.response.PageResponse;
import com.kevin.bankmanagementsys.entity.*;
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
import java.util.ArrayList;
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
        account.setBankName(BankName.valueOf(createAccountRequest.getBankName()));

        accountDAO.save(account);
    }

    public AccountResponse getAccount(Long accountId) throws RuntimeException {
        Account account = accountDAO.findById(accountId).orElseThrow(RuntimeException::new);

        // accountDTO.setBalance(account.getBalance());

        return new AccountResponse(account);
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
        Page<Account> pageAccounts = accountDAO.findByUserAndStatusNot(user, AccountStatus.CLOSED, pageable);
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

    public ListResponse<AccListItem> getListByUserId(Long userId) throws RuntimeException {
        User user = userDAO.findById(userId).orElseThrow(UserNotFoundException::new);
        List<Account> accounts = accountDAO.findByUser(user);

        ListResponse<AccListItem> response = new ListResponse<>();
        List<AccListItem> list = new ArrayList<>();
        for (Account account : accounts) {
            list.add(new AccListItem(account.getId(), account.getAccountNumberWithoutAuth(), account.getBankName()));
        }
        response.setContent(list);
        response.setTotal(accounts.size());
        return response;
    }

    public List<AccountResponse> getAll() throws RuntimeException {
        List<Account> accounts = accountDAO.findAllByStatusNot(AccountStatus.CLOSED);
        List<AccountResponse> accountResponses = new ArrayList<>();
        for (Account account : accounts) {
            AccountResponse ar = new AccountResponse(account);
            ar.setAccountNumber(account.getAccountNumber());
            accountResponses.add(ar);
        }
        return accountResponses;
    }

    public void updateStatus(AccountStatusRequest request) throws RuntimeException {
        Account account = accountDAO.findById(request.getAccountId()).orElseThrow(() -> new RuntimeException("Account Not Found"));
        account.setStatus(AccountStatus.valueOf(request.getAccountStatus()));
        accountDAO.save(account);
    }
}
