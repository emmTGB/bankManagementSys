package com.kevin.bankmanagementsys.controller;

/*
用户请求
POST /users/register: 注册新用户。（弃用，转到auth）
POST /users/login: 用户登录，生成 JWT 或 Session。(弃用，转到auth)
GET /users/{id}: 获取用户信息。
PUT /users/{id}: 更新用户信息（如修改个人资料）。
DELETE /users/{id}: 删除用户（管理员权限）。
 */

import com.kevin.bankmanagementsys.entity.User;
import com.kevin.bankmanagementsys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;


}
