package com.kevin.bankmanagementsys.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccountRequest {
    private AccountDTO accountDTO;
    private AuthDTO authDTO;
}
