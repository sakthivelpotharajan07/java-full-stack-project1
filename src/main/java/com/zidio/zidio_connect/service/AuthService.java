package com.zidio.zidio_connect.service;

import com.zidio.zidio_connect.config.JwtUtil;
import com.zidio.zidio_connect.dto.AuthRequest;
import com.zidio.zidio_connect.dto.AuthResponse;
import com.zidio.zidio_connect.dto.UserRegistrationDto;
import com.zidio.zidio_connect.model.User;
import com.zidio.zidio_connect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;

    public void register(UserRegistrationDto registrationDto) {
        if (userRepository.findByEmail(registrationDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already taken!");
        }
        User user = new User();
        user.setName(registrationDto.getName());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setRole("ROLE_" + registrationDto.getRole().toUpperCase());
        user.setStatus("active");
        userRepository.save(user);
    }

    public AuthResponse login(AuthRequest authRequest) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
        final String token = jwtUtil.generateToken(userDetails);
        User user = userRepository.findByEmail(authRequest.getEmail()).orElseThrow();
        return new AuthResponse(token, user.getUserId(), user.getName(), user.getRole());
    }
}