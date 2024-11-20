package com.kevin.bankmanagementsys.controller.api;

import com.kevin.bankmanagementsys.dto.request.AuthRequest;
import com.kevin.bankmanagementsys.dto.response.AccountResponse;
import com.kevin.bankmanagementsys.dto.response.PageResponse;
import com.kevin.bankmanagementsys.dto.response.UserInfoResponse;
import com.kevin.bankmanagementsys.exception.user.UserNotFoundException;
import com.kevin.bankmanagementsys.service.AccountService;
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

    @Autowired
    private AccountService accountService;

    @GetMapping("/{id}")
    public ResponseEntity<UserInfoResponse> getUser(@PathVariable("id") Long id) {
        try {
            UserInfoResponse userInfoResponse = userService.getUser(id);
            return ResponseEntity.ok(userInfoResponse);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id, @RequestBody AuthRequest authRequest,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid authentication.");
        }

        try {
            if (!userService.authenticate(authRequest)) {
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

    @GetMapping("/{userId}/accounts/{page}")
    public ResponseEntity<PageResponse<AccountResponse>> getAccounts(@PathVariable Long userId,
            @PathVariable int page) {
        try {
            PageResponse<AccountResponse> responseBody = accountService.getPageByUserIdAll(userId, page);
            return ResponseEntity.ok(responseBody);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
