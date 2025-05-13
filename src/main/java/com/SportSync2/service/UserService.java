package com.SportSync2.service;

import com.SportSync2.dto.UserDto;
import com.SportSync2.entity.User;
import com.SportSync2.exception.UserAlreadyExistsException;
import com.SportSync2.mapper.UserMapper;
import com.SportSync2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void registerUser(UserDto userDTO) {
        // Check if a user with the given email already exists
        if (userRepository.findByEmail(userDTO.getEmail()) != null) { // Changed from `isPresent()`
            throw new UserAlreadyExistsException("Email already registered.");
        }

        // Map DTO to entity and save
        User user = UserMapper.toEntity(userDTO);
        userRepository.save(user);
    }
}
