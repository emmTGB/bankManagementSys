package com.kevin.bankmanagementsys.service;

import com.kevin.bankmanagementsys.dto.AuthDTO;
import com.kevin.bankmanagementsys.dto.LoginDTO;
import com.kevin.bankmanagementsys.dto.UserDTO;
import com.kevin.bankmanagementsys.entity.User;
import com.kevin.bankmanagementsys.exception.user.UserAlreadyExistsException;
import com.kevin.bankmanagementsys.exception.user.UserNotFoundException;
import com.kevin.bankmanagementsys.repository.UserDAO;
import com.kevin.bankmanagementsys.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public User register(UserDTO userDTO) throws RuntimeException {
        if(userDAO.existsByUsername(userDTO.getUsername())){
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

    public String login(LoginDTO loginDTO) throws RuntimeException {
        User user = userDAO.findByUsername(loginDTO.getUsername())
                .orElseThrow(UserNotFoundException::new);

        if(!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid password");
        }

        return jwtTokenProvider.createToken(user.getUsername());
    }

    public boolean authenticate(AuthDTO authDTO) throws RuntimeException {
        User user = userDAO.findById(authDTO.getId())
                .orElseThrow(UserNotFoundException::new);

        return !passwordEncoder.matches(authDTO.getPassword(), user.getPassword());
    }
}
