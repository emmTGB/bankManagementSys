package com.kevin.bankmanagementsys.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

//员工

@Entity
@Getter
@Setter
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 主键 ID

    @Column(nullable = false)
    private String fullName; // 员工全名

    @Column(nullable = false, unique = true)
    private String username; // 员工用户名（唯一）

    @Column(nullable = false)
    private String password; // 员工密码（加密）

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeRole role; // 员工角色（经理、出纳等）

    @Column()
    private String phone; // 员工电话

    @Column()
    private String email; // 员工邮箱

    // Getter 和 Setter 省略
}
