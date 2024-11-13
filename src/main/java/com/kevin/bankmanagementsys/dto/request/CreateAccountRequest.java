package com.kevin.bankmanagementsys.dto.request;

import com.kevin.bankmanagementsys.dto.response.AccountDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccountRequest {
    private AccountDTO accountDTO;
    private AuthDTO authDTO;
}
