package com.library.management.mapper;

import com.library.management.entity.User;
import com.library.management.payload.UserRequestDTO;
import com.library.management.payload.UserResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    @Autowired
    ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public User toEntity(UserRequestDTO dto){
        return modelMapper.map(dto, User.class);
    }

    public UserResponseDTO toResponseDTO(User user){
        return modelMapper.map(user, UserResponseDTO.class);
    }
}
