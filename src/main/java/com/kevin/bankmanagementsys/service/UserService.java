package com.kevin.bankmanagementsys.service;

import com.kevin.bankmanagementsys.dto.request.AuthDTO;
import com.kevin.bankmanagementsys.dto.request.LoginDTO;
import com.kevin.bankmanagementsys.dto.request.UserRegisterDTO;
import com.kevin.bankmanagementsys.dto.response.UserInfoDTO;
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

    public User register(UserRegisterDTO userDTO) throws RuntimeException {
        if (userDAO.existsByUsername(userDTO.getUsername())) {
            throw new UserAlreadyExistsException();
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setFullName(userDTO.getFullName());
        user.setPhone(userDTO.getPhone());

        return userDAO.save(user);
    }

    public Map<String, String> login(LoginDTO loginDTO) throws RuntimeException {
        User user = userDAO.findByUsername(loginDTO.getUsername())
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getUsername());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUsername());

        redisSessionService.saveSession(user.getUsername(), refreshToken);

        Map<String, String> map = new HashMap<>();
        map.put("accessToken", accessToken);
        map.put("refreshToken", refreshToken);
        return map;
    }

    public boolean authenticate(AuthDTO authDTO) throws RuntimeException {
        User user = userDAO.findById(authDTO.getId())
                .orElseThrow(UserNotFoundException::new);

        return !passwordEncoder.matches(authDTO.getPassword(), user.getPassword());
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

    public UserInfoDTO getUser(Long id) {
        User user = userDAO.findById(id)
                .orElseThrow(UserNotFoundException::new);
        UserInfoDTO userInfoDTO = new UserInfoDTO(user);

        return userInfoDTO;
    }

    public void deleteUser(Long id) throws RuntimeException {
        if (userDAO.existsById(id))
            userDAO.deleteById(id);
        throw new UserNotFoundException();
    }
}
