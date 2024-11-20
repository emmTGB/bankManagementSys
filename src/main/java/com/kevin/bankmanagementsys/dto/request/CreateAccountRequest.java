package com.kevin.bankmanagementsys.dto.request;

import com.kevin.bankmanagementsys.dto.response.AccountResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccountRequest {
    private AccountResponse accountResponse;
    private AuthRequest authRequest;
}
