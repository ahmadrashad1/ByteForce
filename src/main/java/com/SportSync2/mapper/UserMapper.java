package com.SportSync2.mapper;

import com.SportSync2.dto.UserDto;
import com.SportSync2.entity.User;

public class UserMapper {

    public static User toEntity(UserDto userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setVerified(false); // Default not verified
        return user;
    }

    public static UserDto toDTO(User user) {
        return new UserDto(user.getEmail(), user.getPassword());
    }
}
