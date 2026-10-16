package com.library.management.payload;

import com.library.management.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private Role role;
}
