package com.kevin.bankmanagementsys.repository;

import com.kevin.bankmanagementsys.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeDAO extends JpaRepository<Employee, Long> {
    Optional<Employee> findByUsername(String username);

    boolean existsByUsername(String username);
}
