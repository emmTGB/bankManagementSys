package com.kevin.bankmanagementsys.repository;

import com.kevin.bankmanagementsys.entity.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserDAO extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    List<User> findByUsernameContaining(String username);

    boolean existsByUsername(String username);

    Optional<User> findById(Long id);
}
