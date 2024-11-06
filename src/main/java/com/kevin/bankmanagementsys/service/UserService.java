package com.kevin.bankmanagementsys.service;

import com.kevin.bankmanagementsys.entity.User;
import com.kevin.bankmanagementsys.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    public boolean isExist(String username) {
        return userDAO.findByUsername(username) != null;
    }

    public User get(String username){
        return userDAO.findByUsername(username);
    }

}
