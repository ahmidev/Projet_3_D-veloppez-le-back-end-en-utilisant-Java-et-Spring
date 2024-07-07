package com.projet3.service;

import com.projet3.dto.Mapper;
import com.projet3.dto.UserDTO;
import com.projet3.model.User;
import com.projet3.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final Mapper mapper;



    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return mapper.toUserDTO(user);
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email %s ".formatted(email)));
        return mapper.toUserDTO(user);
    }


}
