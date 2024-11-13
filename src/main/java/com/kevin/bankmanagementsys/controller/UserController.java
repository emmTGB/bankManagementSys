package com.kevin.bankmanagementsys.controller;

import com.kevin.bankmanagementsys.dto.request.AuthDTO;
import com.kevin.bankmanagementsys.dto.response.UserInfoDTO;

/*
用户请求
POST /users/register: 注册新用户。（弃用，转到auth）
POST /users/login: 用户登录，生成 JWT 或 Session。(弃用，转到auth)
GET /users/{id}: 获取用户信息。
PUT /users/{id}: 更新用户信息（如修改个人资料）。
DELETE /users/{id}: 删除用户（管理员权限）。
 */

import com.kevin.bankmanagementsys.entity.User;
import com.kevin.bankmanagementsys.exception.user.UserNotFoundException;
import com.kevin.bankmanagementsys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserInfoDTO> getUser(@PathVariable("id") Long id) {
        try {
            UserInfoDTO userInfoDTO = userService.getUser(id);
            return ResponseEntity.ok(userInfoDTO);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id, @RequestBody AuthDTO authDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid authentication.");
        }

        try {
            if (!userService.authenticate(authDTO)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password.");
            }
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
