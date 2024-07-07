package com.projet3.service;

import com.projet3.dto.UserDTO;
import com.projet3.dto.UserLoginDTO;
import com.projet3.dto.UserRegisterDTO;
import com.projet3.model.AuthSuccess;
import com.projet3.model.User;
import com.projet3.repository.UserRepository;
import com.projet3.security.JwtTokenProvider;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public AuthSuccess register(UserRegisterDTO userRegisterDTO) {
        if (userRepository.existsByEmail(userRegisterDTO.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = new User();
        user.setEmail(userRegisterDTO.getEmail());
        user.setName(userRegisterDTO.getName());
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        User createdUser = userRepository.save(user);
        String token = tokenProvider.generateToken(createdUser);
        return new AuthSuccess(token);
    }


    public AuthSuccess login(UserLoginDTO userLoginDTO) {
        User existingUser = findByEmail(userLoginDTO.getEmail());
        if (existingUser == null || !passwordEncoder.matches(userLoginDTO.getPassword(), existingUser.getPassword())) {
            throw new UsernameNotFoundException("Invalid email or password");
        }
        String token = tokenProvider.generateToken(existingUser);
        return new AuthSuccess(token);
    }

    public UserDTO getUserDTO(User currentUser) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(currentUser.getId());
        userDTO.setEmail(currentUser.getEmail());
        userDTO.setName(currentUser.getName());
        userDTO.setCreatedAt(currentUser.getCreatedAt());
        userDTO.setUpdatedAt(currentUser.getUpdatedAt());
        return userDTO;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
