package com.kevin.bankmanagementsys.repository;

import com.kevin.bankmanagementsys.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDAO extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    List<User> findByUsernameContaining(String username);
}
