package com.library.management.service;

import com.library.management.payload.AuthResponseDTO;
import com.library.management.payload.LoginRequestDTO;
import com.library.management.security.CustomUserDetailsService;
import com.library.management.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponseDTO login(LoginRequestDTO request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        // Get the authenticated username/email
        String email = authentication.getName();

        // Load the actual UserDetails object
        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername(email);

        // Generate JWT
        String token = jwtService.generateToken(userDetails);

        return new AuthResponseDTO(token);
    }
}
