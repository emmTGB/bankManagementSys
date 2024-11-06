package com.kevin.bankmanagementsys.entity;

//用户

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 主键 ID

    @Column(nullable = false, unique = true)
    private String username;  // 用户名，唯一

    @Column(nullable = false)
    private String password;  // 加密后的密码

    @Column(nullable = false)
    private String role;  // 用户角色（"ADMIN", "CUSTOMER"）

    @Column(nullable = false)
    private String fullName;  // 用户全名

    @Column(nullable = false)
    private String email;  // 用户邮箱

    @Column(nullable = true)
    private String phone;  // 用户电话

    // Getter 和 Setter 省略
}