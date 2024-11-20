package com.kevin.bankmanagementsys.dto.response;

import com.kevin.bankmanagementsys.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoResponse {
    
    public UserInfoResponse(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
    }

    private Long id;

    @NotNull(message = "Username can not be null")
    private String username;

    @NotNull(message = "Full name can not be null")
    private String fullName;

    @Email(message = "Email must be valid")
    private String email;
    private String phone;
}
