package com.kevin.bankmanagementsys.exception.user;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(){
        super("User not found");
    }
}
