package com.library.management.service;

import com.library.management.payload.UserRequestDTO;
import com.library.management.payload.UserResponseDTO;

public interface UserService {
    UserResponseDTO registerUser(UserRequestDTO userRequestDTO);
}
