package com.kevin.bankmanagementsys.exception.user;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException() {
        super("User already exists");
    }
}
