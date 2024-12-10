package com.kevin.bankmanagementsys.repository;

import com.kevin.bankmanagementsys.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDAO extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    List<User> findByUsernameContaining(String username);

    boolean existsByUsername(String username);

    boolean existsById(Long id);

    Optional<User> findById(Long id);

    void deleteById(Long id);
}
