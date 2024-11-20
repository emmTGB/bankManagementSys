package com.kevin.bankmanagementsys.repository;

import com.kevin.bankmanagementsys.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeDAO extends JpaRepository<Employee, Long> {
    Optional<Employee> findByUsername(String username);

    boolean existsByUsername(String username);
}
