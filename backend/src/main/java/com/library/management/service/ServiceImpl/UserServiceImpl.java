package com.library.management.service.ServiceImpl;

import com.library.management.entity.Role;
import com.library.management.entity.User;
import com.library.management.mapper.UserMapper;
import com.library.management.payload.UserRequestDTO;
import com.library.management.payload.UserResponseDTO;
import com.library.management.repository.UserRepository;
import com.library.management.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO) throws IllegalAccessException {
        logger.info("Registering user with email: {}", userRequestDTO.getEmail());

        if(userRepository.existsByEmail(userRequestDTO.getEmail())){
            throw new IllegalAccessException("Email is al ready registered");
        }

        if (userRepository.existsByUsername(userRequestDTO.getUsername())){
            throw new IllegalAccessException("Username is al ready taken");
        }

        User user = userMapper.toEntity(userRequestDTO);
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);

        logger.info("Username is registered successfully with ID : {}", savedUser.getId());
        return userMapper.toResponseDTO(savedUser);
    }
}
