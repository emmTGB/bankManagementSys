package com.kevin.bankmanagementsys.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountStatusRequest {
    private Long accountId;
    private String accountStatus;
}
