package com.kevin.bankmanagementsys.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

// 登录 post /auth/user/login

@Getter
@Setter
public class LoginDTO {
    @NotNull(message = "Username can not be null")
    private String username;

    @NotNull(message = "Password cannot be null")
    private String password;
}
