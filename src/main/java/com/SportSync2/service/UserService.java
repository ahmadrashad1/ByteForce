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
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already registered.");
        }

        User user = UserMapper.toEntity(userDTO);
        userRepository.save(user);
    }
}
