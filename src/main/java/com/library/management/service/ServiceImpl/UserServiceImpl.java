package com.library.management.service.ServiceImpl;

import com.library.management.mapper.UserMapper;
import com.library.management.payload.UserRequestDTO;
import com.library.management.payload.UserResponseDTO;
import com.library.management.repository.UserRepository;
import com.library.management.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

//    private final PasswordEncoder

    @Override
    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO) {
        return null;
    }
}
