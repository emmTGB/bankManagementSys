package com.kevin.bankmanagementsys.service;

import com.kevin.bankmanagementsys.dto.request.AuthRequest;
import com.kevin.bankmanagementsys.dto.request.LoginRequest;
import com.kevin.bankmanagementsys.dto.request.UserRegisterRequest;
import com.kevin.bankmanagementsys.dto.response.UserInfoResponse;
import com.kevin.bankmanagementsys.entity.User;
import com.kevin.bankmanagementsys.exception.user.UserAlreadyExistsException;
import com.kevin.bankmanagementsys.exception.user.UserNotFoundException;
import com.kevin.bankmanagementsys.repository.UserDAO;
import com.kevin.bankmanagementsys.security.JwtTokenProvider;
import com.kevin.bankmanagementsys.security.RedisSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RedisSessionService redisSessionService;

    public void register(UserRegisterRequest registerRequest) throws RuntimeException {
        if (userDAO.existsByUsername(registerRequest.getUsername())) {
            throw new UserAlreadyExistsException();
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setFullName(registerRequest.getFullName());
        user.setPhone(registerRequest.getPhone());

        userDAO.save(user);
    }

    public Map<String, String> login(LoginRequest loginRequest) throws RuntimeException {
        User user = userDAO.findByUsername(loginRequest.getUsername())
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getUsername());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUsername());

        redisSessionService.saveSession(user.getUsername(), refreshToken);

        Map<String, String> map = new HashMap<>();
        map.put("accessToken", accessToken);
        map.put("refreshToken", refreshToken);
        map.put("id", String.valueOf(user.getId()));
        return map;
    }

    public boolean authenticate(AuthRequest authRequest) throws RuntimeException {
        User user = userDAO.findById(authRequest.getId())
                .orElseThrow(UserNotFoundException::new);

        return !passwordEncoder.matches(authRequest.getPassword(), user.getPassword());
    }

    public Map<String, String> refresh(String refreshToken) throws RuntimeException {
        final long REFRESH_THRESHOLD = 5 * 24 * 60 * 60;
        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);

        if (redisSessionService.validateRefreshToken(username, refreshToken)) { // 验证refresh token是否过期
            String newAccessToken = jwtTokenProvider.createAccessToken(username);
            Map<String, String> map = new HashMap<>();
            map.put("accessToken", newAccessToken);
            if (redisSessionService.getRefreshTokenExpiry(username) <= REFRESH_THRESHOLD) { // refresh token即将到期，进行续期
                String newRefreshToken = jwtTokenProvider.createRefreshToken(username);
                redisSessionService.saveSession(username, newRefreshToken);
                map.put("refreshToken", newRefreshToken);
            }
            return map;
        } else {
            throw new RuntimeException("Invalid refresh token");
        }
    }

    public void logout(String refreshToken) throws RuntimeException {
        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        redisSessionService.invalidateSession(username);
    }

    public UserInfoResponse getUser(Long id) {
        User user = userDAO.findById(id)
                .orElseThrow(UserNotFoundException::new);

        return new UserInfoResponse(user);
    }

    public void deleteUser(Long id) throws RuntimeException {
        if (userDAO.existsById(id))
            userDAO.deleteById(id);
        throw new UserNotFoundException();
    }
}
